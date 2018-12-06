package com.cad.data.threads;

import com.cad.data.database.mysql.dao.MysqlDataDao;
import com.cad.data.database.mysql.dao.IbdTransterDataDao;
import com.cad.data.domain.IbdTransterData;
import com.cad.data.enums.*;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MysqlDataThread extends AbstractSource implements Configurable, EventDrivenSource {

    private static final String CONFIG_TRANSFER_STATUS = "transferStatus";

    private static final String CONFIG_START_TIME = "startTime";

    private static final String CONFIG_END_TIME = "endTime";

    private static final String CONFIG_THREAD_COUNT = "threadCount";

    private static final String CONFIG_THREAD_INTERAL_SEC = "threadInteralSec";

    private static final String CONFIG_QUERY_INTERAL_SEC = "queryInteralSec";

    private static final String CONFIG_AREATYPE = "areaType";

    private static final String CONFIG_LOGTYPE = "logType";

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

    // 导数据的开始时间 "yyyy-MM-dd HH:mm:ss" 格式
    private String startTime = null;

    // 导数据的结束时间 "yyyy-MM-dd HH:mm:ss" 格式
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

    // 地域配置信息
    private String areaType = null;

    // 日志类型配置信息 sqlStr
    private String logType = null;


    final Logger LOGGER = LoggerFactory.getLogger ( MysqlDataThread.class );


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

            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_AREATYPE ) ) ) {
                areaType = flumeContext.getString ( CONFIG_AREATYPE );
            } else if ( configBundle.containsKey ( CONFIG_AREATYPE ) ) {
                areaType = configBundle.getString ( CONFIG_AREATYPE );

            }

            if ( flumeContext != null && StringUtils.isNotEmpty ( flumeContext.getString ( CONFIG_LOGTYPE ) ) ) {
                logType = flumeContext.getString ( CONFIG_LOGTYPE );
            } else if ( configBundle.containsKey ( CONFIG_LOGTYPE ) ) {
                logType = configBundle.getString ( CONFIG_LOGTYPE );

            }

            DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
            if ( StringUtils.isNotEmpty ( startTime ) ) {
                startTimeMillis = timeDf.parse ( startTime ).getTime ();
            }
            if ( StringUtils.isNotEmpty ( endTime ) ) {
                endTimeMillis = timeDf.parse ( endTime ).getTime ();
            }
            // 如果不是 renew 状态，则从mysql的导入数据记录中找到上次最晚的传输时间
            if ( !TransferStatusEnum.RENEW.getCode ().equals ( transferStatus ) ) {
                // 查询数据库，找出导数据记录表中，最晚的一条记录
                IbdTransterData ibdTransterData = new IbdTransterData ( areaType, logType, DatabaseTypeEnum.MYSQL.getCode () );
                String lastTransferTime = IbdTransterDataDao.getLatestTransferTime ( ibdTransterData );
                if ( StringUtils.isNotEmpty ( lastTransferTime ) ) {
                    startTime = lastTransferTime;
                    startTimeMillis = timeDf.parse ( startTime ).getTime ();
                }
            }
            LOGGER.error ( "@@@ startTime : " + startTime );
            LOGGER.error ( "@@@ startTimeMillis : " + startTimeMillis );
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
            try {
                // 不同线程中间休息5 秒
                Thread.sleep ( 5000l );
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            scheduledExecutorService.scheduleWithFixedDelay ( new Runnable () {
                @Override
                public void run() {
                    Long curCount = count.get ();
                    // 计算结束时间
                    Long curEnd = startTimeMillis + (curCount + 1) * queryInteralSec * 1000L;

                    // 当前时间 要大于 结束时间 ，避免数据重复抽取 ，2 秒是为了避免时间偏差
                    if ( System.currentTimeMillis () > curEnd + 2000L ) {
                        try {
                            long begin = startTimeMillis + curCount * queryInteralSec * 1000L;
                            long end = begin + queryInteralSec * 1000L;
                            curCount = count.addAndGet ( 1 );

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
                            IbdTransterData ibdTransterData = new IbdTransterData ( areaType, logType, DatabaseTypeEnum.MYSQL.getCode () );
                            ibdTransterData.setConditionStime ( timeStr_begin );
                            ibdTransterData.setConditionEtime ( timeStr_end );
                            ibdTransterData.setDataCount ( 0l );
                            String thisLogId = IbdTransterDataDao.addIbdTransterData ( ibdTransterData );
                            // 2: 查询数据，并导入到channel中
                            List<Object[]> dataList = null;
                            try {
                                dataList = MysqlDataDao.getMysqlData ( flumeContext, timeStr_begin, timeStr_end );
                                LOGGER.info ( "@@@ timeStr_begin : " + timeStr_begin + "   " + timeStr_end + "  count= " + dataList.size () );
                            } catch (Exception e) {
                                e.printStackTrace ();
                                LOGGER.error ( "@@@ search error : " + e.getMessage () );
                                ibdTransterData.setId ( thisLogId );
                                ibdTransterData.setStatus ( DataStatusEnum.ERROR_QUERY.getCode () );
                                ibdTransterData.setRemark ( DataStatusEnum.ERROR_QUERY.getName () );
                                IbdTransterDataDao.updateIbdTransterDataById ( ibdTransterData );
                                return;
                            }
                            try {
                                sendMassageTochannel ( dataList );
                            } catch (Exception e) {
                                e.printStackTrace ();
                                LOGGER.error ( "@@@ import error : " + e.getMessage () );
                                ibdTransterData.setId ( thisLogId );
                                ibdTransterData.setStatus ( DataStatusEnum.ERROR_IMPORT.getCode () );
                                ibdTransterData.setDataCount ( dataList != null ? (long) dataList.size () : 0l );
                                ibdTransterData.setRemark ( DataStatusEnum.ERROR_IMPORT.getName () );
                                IbdTransterDataDao.updateIbdTransterDataById ( ibdTransterData );
                                return;
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
                            LOGGER.error ( "@@@ system error : " + errorParam );
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
                    List<IbdTransterData> dataList = IbdTransterDataDao.getExceptionRetryDataByLogType ( areaType, logType, stime, etime );
                    // 集中处理“导入失败”的记录！
                    if ( dataList != null && dataList.size () > 0 ) {
                        for (IbdTransterData thisIbdTransterData : dataList) {
                            if ( thisIbdTransterData != null ) {
                                List<Object[]> retryDataList = null;
                                try {
                                    String thisConditionStime = thisIbdTransterData.getConditionStime ();
                                    String thisConditionEtime = thisIbdTransterData.getConditionEtime ();
                                    retryDataList = MysqlDataDao.getMysqlData ( flumeContext, thisConditionStime, thisConditionEtime );
                                    sendMassageTochannel ( retryDataList );
                                    // 记录日志（修改成功标记位）
                                    thisIbdTransterData.setDataCount ( retryDataList != null ? (long) retryDataList.size () : 0l );
                                    thisIbdTransterData.setStatus ( DataStatusEnum.SUCCESS.getCode () );
                                    thisIbdTransterData.setRetry ( thisIbdTransterData.getRetry () + 1 );
                                    IbdTransterDataDao.updateIbdTransterDataById ( thisIbdTransterData );
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                    String errorParam = "[AreaType: " + thisIbdTransterData.getAreaType () + ", logType: " + thisIbdTransterData.getLogType () + " , stime: " + thisIbdTransterData.getConditionStime () + ", etime: " + thisIbdTransterData.getConditionEtime () + "]";
                                    LOGGER.error ( "@@@ retry error : " + errorParam );
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

        super.start ();
    }

    private void sendMassageTochannel(List<Object[]> dataList) {
        if ( dataList != null && dataList.size () > 0 ) {
            List<Event> retryEventList = new ArrayList<Event> ();
            for (Object[] thisLogReq : dataList) {
                if ( thisLogReq != null ) {
                    Event thisEvent = EventBuilder.withBody ( Arrays.toString ( thisLogReq ), Charset.forName ( "UTF-8" ) );
                    retryEventList.add ( thisEvent );
                }
            }
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


    public static void main(String[] args) throws EventDeliveryException, InterruptedException {
//        Runtime.getRuntime().addShutdownHook(new Thread(){
//            public void run(){
//                try{
//                    mongoClient.close();
//                    System.out.println("The JVM Hook is execute");
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


}
