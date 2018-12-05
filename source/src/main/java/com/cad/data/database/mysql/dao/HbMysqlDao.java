package com.cad.data.database.mysql.dao;

import com.cad.data.database.SqlMapUtils;
import com.cad.data.database.mysql.utils.JDBCTools;
import com.cad.data.domain.AlarmeventLog;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;

import java.util.List;


public class HbMysqlDao {

    /**
     * AlarmeventLog  报警日志
     */
    public static List<AlarmeventLog> getAlarmeventLog(Context flumeCtx, String stime, String etime) {
        if ( StringUtils.isNotEmpty ( stime ) ) {
            try {
                String sqlStr = SqlMapUtils.getLmSqlMap ().get ( "hb_alarmevent_log" );
                sqlStr = sqlStr.replace ( "${stime}", stime ).replace ( "${etime}", etime );
                List<AlarmeventLog> resultList = JDBCTools.getHbQueryRunner ( flumeCtx ).query ( sqlStr, new BeanListHandler<> ( AlarmeventLog.class ) );
                // 时间格式有问题，需要转换
                if ( resultList != null && resultList.size () > 0 ) {
                    for (AlarmeventLog thisLogReq : resultList) {
                        if ( StringUtils.isNotEmpty ( thisLogReq.getLastModifyTime () ) && thisLogReq.getLastModifyTime ().length () > 19 ) {
                            thisLogReq.setLastModifyTime ( thisLogReq.getLastModifyTime ().substring ( 0, 19 ) );
                        }
                    }
                }
                return resultList;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return null;
    }

    public static void main(String[] args) {
//		List<BusinessLogReq> resList =  getUserBusinessLogs(null, "2018-03-30 09:30:00", "2018-03-30 09:31:00");
//		System.out.println("~~~@@@~~~ query result size == " + resList.size());
        System.out.println ( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#############  " + "2017-06-04 04:35:27.0".endsWith ( ".0" ) );
        System.out.println ( "~~###~~~  " + "2017-06-04 04:35:27.0".substring ( 0, 19 ) );
    }

}
