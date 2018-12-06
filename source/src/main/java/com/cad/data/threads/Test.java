package com.cad.data.threads;

import com.cad.data.database.mysql.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {


    public static void main(String[] args) {

        try {
            DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
            if ( StringUtils.isNotEmpty ( "2018-01-01 00:00:01" ) ) {
                System.out.println ( timeDf.parse ( "2018-01-01 00:00:01" ).getTime () );
            }
            JDBCTools.getIbdQueryRunner ().update ( "delete from   ibd_transter_data where status =1 and condition_stime<='" + timeDf.format ( timeDf.parse ( "2018-01-01 00:00:01" ).getTime () - 2 * 24 * 60 * 60 * 1000l ) + "'" );

            System.out.println ( System.currentTimeMillis () );
            System.out.println ( timeDf.parse ( "2018-01-01 00:00:01" ).getTime () );

            System.out.println ( timeDf.format ( new Date ().getTime () - 5* 24*60*60*1000l ) );


//            String sql = "UPDATE ibd_transter_data SET ";
//
//            sql += " data_count = " + 2 + ", ";
//            sql += " WHERE id = '" + 222222 + "'";
//
//            System.out.println ( sql );
//
//            System.currentTimeMillis ();
//
//
//            String sqlStr = "select * from tbalarmevent where eventStartTime >='start_time' and eventStartTime< 'end_time'";
//            sqlStr = sqlStr.replace ( "start_time", "2018-06-01 00:00:00" ).replace ( "end_time", "2018-06-01 02:00:00" );
//            System.out.println ( sqlStr );
//
//
//            List<Object[]> resultList = JDBCTools.getIbdQueryRunner ().query ( sqlStr, new ArrayListHandler () );
//
//            // 时间格式有问题，需要转换
//            if ( resultList != null && resultList.size () > 0 ) {
//                for (Object[] objects : resultList) {
//
//                    System.out.println ( Arrays.toString ( objects ) );
//                }
//
//            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
