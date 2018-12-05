package com.cad.data.threads;

import com.alibaba.fastjson.JSONObject;
import com.cad.data.database.SqlMapUtils;
import com.cad.data.database.mysql.dao.HbMysqlDao;
import com.cad.data.database.mysql.dao.IbdTransterDataDao;
import com.cad.data.domain.AlarmeventLog;
import com.cad.data.domain.IbdTransterData;
import com.cad.data.enums.*;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义 flume source  实现实时抽取 MySQL 的数据
 */
public class AlarmEventThread extends AbstractSource implements Configurable, EventDrivenSource {

    private static final String CONFIG_TRANSFER_STATUS = "threads.carlink.tbalarmevent.transferStatus";

    private static final String CONFIG_START_TIME = "threads.carlink.tbalarmevent.startTime";

    private static final String CONFIG_END_TIME = "threads.carlink.tbalarmevent.endTime";

    private static final String CONFIG_THREAD_COUNT = "threads.carlink.tbalarmevent.threadCount";

    private static final String CONFIG_THREAD_INTERAL_SEC = "threads.carlink.tbalarmevent.threadInteralSec";

    private static final String CONFIG_QUERY_INTERAL_SEC = "threads.carlink.tbalarmevent.queryInteralSec";

    // error重试机制，init时间为 30min 
    private static final Integer errorRetryInitSec = 30 * 60;

    // error重试机制，时间间隔为 30min
    private static final Integer errorRetryInteralSec = 30 * 60;

    // flume框架，配置文件
    private Context flumeContext;

    //读取本地的配置文件
    private static ResourceBundle configBundle = ResourceBundle.getBundle ( "source-config" );

    // 循环计数器（多线程共用）
    private AtomicLong count = new AtomicLong ( 0 );

    // 启动的多线程数
    private Integer threadCount = null;

    // 同一线程，两次运行的间隔时间（秒数）
    private Integer threadInteralSec = null;

    // 每次查询多长时间间隔内的数据（秒数）
    private Integer queryInteralSec = null;

    // 导数据的方式状态（normal-正常 ; renew-重传）
    private String transferStatus = null;

    // 导数据的开始时间 "yyyy-MM-dd_HH:mm:ss" 格式
    private String startTime = null;

    // 导数据的结束时间 "yyyy-MM-dd_HH:mm:ss" 格式
    private String endTime = null;

    // 导数据的开始时间（毫秒数）
    private Long startTimeMillis = null;

    // 导数据的结束时间（毫秒数）
    private Long endTimeMillis = null;

    // 线程池（导数据）
    private ScheduledExecutorService scheduledExecutorService;

    // 线程池（错误重试机制）
    private ScheduledExecutorService errorRetryExecutorService;

    // 发送数据 flume-channel
    private ChannelProcessor channelProcessor;

    final Logger LOGGER = LoggerFactory.getLogger ( AlarmEventThread.class );


    /**
     * 加载flume的mongodb数据源配置信息
     */
    @Override
    public void configure(Context context) {
        try {
            flumeContext = context;
            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_TRANSFER_STATUS ) ) ) {
                transferStatus = flumeContext.getString ( CONFIG_TRANSFER_STATUS );
            } else if ( configBundle.containsKey ( CONFIG_TRANSFER_STATUS ) ) {
                transferStatus = configBundle.getString ( CONFIG_TRANSFER_STATUS );
            }
            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_START_TIME ) ) ) {
                startTime = flumeContext.getString ( CONFIG_START_TIME );
            } else if ( configBundle.containsKey ( CONFIG_START_TIME ) ) {
                startTime = configBundle.getString ( CONFIG_START_TIME );

            }
            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_END_TIME ) ) ) {
                endTime = flumeContext.getString ( CONFIG_END_TIME );
            } else if ( configBundle.containsKey ( CONFIG_END_TIME ) ) {
                endTime = configBundle.getString ( CONFIG_END_TIME );
            }
            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_THREAD_COUNT ) ) ) {
                threadCount = flumeContext.getInteger ( CONFIG_THREAD_COUNT );
            } else if ( configBundle.containsKey ( CONFIG_THREAD_COUNT ) ) {
                threadCount = Integer.parseInt ( configBundle.getString ( CONFIG_THREAD_COUNT ) );
            }

            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_THREAD_INTERAL_SEC ) ) ) {
                threadInteralSec = flumeContext.getInteger ( CONFIG_THREAD_INTERAL_SEC );
            } else if ( configBundle.containsKey ( CONFIG_THREAD_INTERAL_SEC ) ) {
                threadInteralSec = Integer.parseInt ( configBundle.getString ( CONFIG_THREAD_INTERAL_SEC ) );

            }
            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_QUERY_INTERAL_SEC ) ) ) {
                queryInteralSec = flumeContext.getInteger ( CONFIG_QUERY_INTERAL_SEC );
            } else if ( configBundle.containsKey ( CONFIG_QUERY_INTERAL_SEC ) ) {
                queryInteralSec = Integer.parseInt ( configBundle.getString ( CONFIG_QUERY_INTERAL_SEC ) );

            }

            DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd_HH:mm:ss" );

            if ( StringUtils.isNotEmpty ( startTime ) ) {
                startTimeMillis = timeDf.parse ( startTime ).getTime ();
            }
            if ( StringUtils.isNotEmpty ( endTime ) ) {
                endTimeMillis = timeDf.parse ( endTime ).getTime ();
            }

            // 如果不是 renew 状态，则从mysql的导入数据记录中找到上次最晚的传输时间
            if ( !TransferStatusEnum.RENEW.getCode ().equals ( transferStatus ) ) {
                // 查询数据库，找出导数据记录表中，最晚的一条记录
                IbdTransterData ibdTransterData = new IbdTransterData ();
                ibdTransterData.setAreaType ( AreaTypeEnum.HB.getCode () );
                ibdTransterData.setLogType ( LogTypeEnum.ALARMEVENT.getCode () );
                ibdTransterData.setDataSource ( DatabaseTypeEnum.MYSQL.getCode () );
                String lastTransferTime = IbdTransterDataDao.getLatestTransferTime ( ibdTransterData );
                if ( StringUtils.isNotEmpty ( lastTransferTime ) ) {
                    startTime = lastTransferTime.replace ( " ", "_" );
                    startTimeMillis = timeDf.parse ( startTime ).getTime ();
                }
            }
            LOGGER.error ( "init conf : " + startTime + "|" + endTime );

        } catch (Exception e) {
            e.printStackTrace ();
            LOGGER.error ( "加载配置文件失败 : " + e.getMessage () );
        }
    }

    /**
     * Starts the source. Starts the metrics counter.
     */
    @Override
    public void start() {
        channelProcessor = this.getChannelProcessor ();
        scheduledExecutorService = Executors.newScheduledThreadPool ( threadCount );
        // 循环启动多线程
        for (int k = 0; k < threadCount; k++) {
            //10秒钟运行一个
            try {
                Thread.sleep ( 10000l );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            scheduledExecutorService.scheduleWithFixedDelay ( new Runnable () {
                @Override
                public void run() {
                    Long curCount = count.get ();
                    // 这里是计算结束时间
                    Long curEnd = startTimeMillis + (curCount + 1) * queryInteralSec * 1000L;
                    if ( (System.currentTimeMillis () - 5 * 60 * 1000L) >= curEnd ) {
                        curCount = count.addAndGet ( 1 );
                        try {
                            long begin = startTimeMillis + (curCount - 1) * queryInteralSec * 1000L;
                            long end = begin + queryInteralSec * 1000L;
                            // 如果设置了“结束时间”
                            if ( endTimeMillis != null && endTimeMillis > 0 ) {
                                if ( begin >= endTimeMillis ) {
                                    return;
                                } else {
                                    if ( end >= endTimeMillis ) {
                                        end = endTimeMillis;
                                    }
                                }
                            }
                            DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
                            String timeStr_begin = timeDf.format ( new Date ( begin ) );
                            String timeStr_end = timeDf.format ( new Date ( end ) );
                            // 1: 先记录日志
                            IbdTransterData ibdTransterData = new IbdTransterData ();
                            ibdTransterData.setAreaType ( AreaTypeEnum.HB.getCode () );
                            ibdTransterData.setLogType ( LogTypeEnum.ALARMEVENT.getCode () );
                            ibdTransterData.setDataSource ( DatabaseTypeEnum.MYSQL.getCode () );
                            ibdTransterData.setTableName ( SqlMapUtils.HB_ALARMEVENT_LOG_TB );
                            ibdTransterData.setConditionStime ( timeStr_begin );
                            ibdTransterData.setConditionEtime ( timeStr_end );
                            String thisLogId = IbdTransterDataDao.addIbdTransterData ( ibdTransterData );
                            // 2: 查询数据，并导入到channel中
                            List<AlarmeventLog> dataList = null;
                            try {
                                dataList = HbMysqlDao.getAlarmeventLog ( flumeContext, timeStr_begin, timeStr_end );
                            } catch (Exception e) {
                                e.printStackTrace ();
                                LOGGER.error ( "@@@ 查询失败 : " + e.getMessage () );
                                // 查询失败！
                                ibdTransterData.setId ( thisLogId );
                                ibdTransterData.setStatus ( DataStatusEnum.ERROR_QUERY.getCode () );
                                ibdTransterData.setRemark ( DataStatusEnum.ERROR_QUERY.getName () );
                                IbdTransterDataDao.updateIbdTransterDataById ( ibdTransterData );
                                return;
                            }
                            if ( dataList != null && dataList.size () > 0 ) {
                                List<Event> eventList = new ArrayList<Event> ();
                                for (AlarmeventLog thisLogReq : dataList) {
                                    if ( thisLogReq != null ) {
                                        Event thisEvent = EventBuilder.withBody ( JSONObject.toJSONString ( thisLogReq ), Charset.forName ( "UTF-8" ) );
                                        eventList.add ( thisEvent );
                                    }
                                }
                                try {
                                    // channelProcessor.processEventBatch(eventList);
                                    if ( eventList.size () > 10000 ) {
                                        int startIdx = 0;
                                        int importCount = 10000;
                                        for (; startIdx < eventList.size (); startIdx += 10000) {
                                            if ( startIdx + 10000 > eventList.size () ) {
                                                importCount = eventList.size () - startIdx;
                                            }
                                            channelProcessor.processEventBatch ( eventList.subList ( startIdx, startIdx + importCount ) );
                                        }

                                    } else {
                                        channelProcessor.processEventBatch ( eventList );
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    LOGGER.error ( "@@@ 导入失败 : " + e.getMessage () );
                                    // 导入失败！
                                    ibdTransterData.setId ( thisLogId );
                                    ibdTransterData.setStatus ( DataStatusEnum.ERROR_IMPORT.getCode () );
                                    ibdTransterData.setDataCount ( dataList != null ? (long) dataList.size () : 0l );
                                    ibdTransterData.setRemark ( DataStatusEnum.ERROR_IMPORT.getName () );
                                    IbdTransterDataDao.updateIbdTransterDataById ( ibdTransterData );
                                    return;
                                }
                            }
                            // 3： 再记录日志（修改成功标记位）
                            ibdTransterData.setId ( thisLogId );
                            ibdTransterData.setDataCount ( dataList != null ? (long) dataList.size () : 0l );
                            ibdTransterData.setStatus ( DataStatusEnum.SUCCESS.getCode () );
                            IbdTransterDataDao.updateIbdTransterDataById ( ibdTransterData );
                        } catch (Exception e) {
                            e.printStackTrace ();
                            long errorBegin = startTimeMillis + (curCount - 1) * queryInteralSec * 1000L;
                            String errorParam = "[errorBegin:" + errorBegin + ", startTime:'" + startTime + "', queryInteralSec:" + queryInteralSec + "S, curCount:" + curCount + ", ]";
                            LOGGER.error ( "@@@ 程序异常 : " + errorParam );
                            LOGGER.error ( "@@@ errorMsg: " + e.getMessage () );
                        }
                    }
                }
            }, 10, threadInteralSec, TimeUnit.SECONDS );
        }

        // 错误重试机制线程池
        errorRetryExecutorService = Executors.newScheduledThreadPool ( 1 );
        errorRetryExecutorService.scheduleWithFixedDelay ( new Runnable () {
            @Override
            public void run() {
                try {
                    DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
                    // 查询的起止时间是，12h前-->0.5h前（半个小时前，且12小时以内）
                    String stime = timeDf.format ( new Date ( (new Date ()).getTime () - 12 * 60 * 60 * 1000L ) );
                    String etime = timeDf.format ( new Date ( (new Date ()).getTime () - 30 * 60 * 1000L ) );
                    // error失败的记录（status < 0）或者 init后没有导入的记录（半个小时后仍然没有导入成功）
                    List<IbdTransterData> dataList = IbdTransterDataDao.getExceptionRetryDataByLogType (
                            AreaTypeEnum.HB.getCode (), LogTypeEnum.ALARMEVENT.getCode (), stime, etime );
                    // 集中处理“导入失败”的记录！
                    if ( dataList != null && dataList.size () > 0 ) {
                        for (IbdTransterData thisIbdTransterData : dataList) {
                            if ( thisIbdTransterData != null ) {
                                List<AlarmeventLog> retryDataList = null;
                                try {
                                    String thisConditionStime = thisIbdTransterData.getConditionStime ();
                                    String thisConditionEtime = thisIbdTransterData.getConditionEtime ();
                                    retryDataList = HbMysqlDao.getAlarmeventLog ( flumeContext, thisConditionStime, thisConditionEtime );
                                    if ( retryDataList != null && retryDataList.size () > 0 ) {
                                        List<Event> retryEventList = new ArrayList<Event> ();
                                        for (AlarmeventLog thisLogReq : retryDataList) {
                                            if ( thisLogReq != null ) {
                                                Event thisEvent = EventBuilder.withBody ( JSONObject.toJSONString ( thisLogReq ), Charset.forName ( "UTF-8" ) );
                                                retryEventList.add ( thisEvent );
                                            }
                                        }
                                        // channelProcessor.processEventBatch(retryEventList);
                                        if ( retryEventList.size () > 10000 ) {
                                            int startIdx = 0;
                                            int importCount = 10000;
                                            for (; startIdx < retryEventList.size (); startIdx += 10000) {
                                                if ( startIdx + 10000 > retryEventList.size () ) {
                                                    importCount = retryEventList.size () - startIdx;
                                                }
                                                channelProcessor.processEventBatch ( retryEventList.subList ( startIdx, startIdx + importCount ) );
                                            }
                                        } else {
                                            channelProcessor.processEventBatch ( retryEventList );
                                        }
                                    }
                                    // 记录日志（修改成功标记位）
                                    // thisIbdTransterData.setId(thisIbdTransterData.getId());
                                    thisIbdTransterData.setDataCount ( retryDataList != null ? (long) retryDataList.size () : 0l );
                                    thisIbdTransterData.setStatus ( DataStatusEnum.SUCCESS.getCode () );
                                    thisIbdTransterData.setRetry ( thisIbdTransterData.getRetry () + 1 );
                                    IbdTransterDataDao.updateIbdTransterDataById ( thisIbdTransterData );
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    String errorParam = "[areaType: " + thisIbdTransterData.getAreaType () + ", logType: " + thisIbdTransterData.getLogType () + " , stime: " + thisIbdTransterData.getConditionStime () + ", etime: " + thisIbdTransterData.getConditionEtime () + "]";
                                    LOGGER.error ( "@@@ retry 异常，查询条件 : " + errorParam );
                                    LOGGER.error ( "@@@ retry errorMsg: " + e.getMessage () );
                                    // 记录日志数据（修改"retry"次数，加 1 ）
                                    // thisIbdTransterData.setId(thisIbdTransterData.getId());
                                    thisIbdTransterData.setDataCount ( retryDataList != null ? (long) retryDataList.size () : 0l );
                                    thisIbdTransterData.setRetry ( thisIbdTransterData.getRetry () + 1 );
                                    IbdTransterDataDao.updateIbdTransterDataById ( thisIbdTransterData );
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        }, errorRetryInitSec, errorRetryInteralSec, TimeUnit.SECONDS );

        // start
        super.start ();
    }

    /**
     * Stop the source. Close database connection and stop metrics counter.
     */
    @Override
    public void stop() {
        scheduledExecutorService.shutdown ();
        errorRetryExecutorService.shutdown ();
        super.stop ();
    }

    public static void main(String[] args) {


    }


}
