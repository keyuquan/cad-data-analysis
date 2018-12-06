package com.cad.data.threads;

import com.cad.data.database.mysql.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import java.util.Arrays;
import java.util.List;

public class Test {

    private static QueryRunner HbQueryRunner = null;
    public static void main(String[] args) {

        try {

            HbQueryRunner = JDBCTools.getIbdQueryRunner ();

//            String sqlStr = SqlMapUtils.getLmSqlMap ().get ( "hb_alarmevent_log" );
//            sqlStr = sqlStr.replace ( "${stime}", "2018-06-01 00:00:00" ).replace ( "${etime}", "2018-06-02 00:00:00" );
//            System.out.println ( sqlStr );
//
//            List<Object[]> resultList = HbQueryRunner.query ( sqlStr, new ArrayListHandler() );
//
//            // 时间格式有问题，需要转换
//            if ( resultList != null && resultList.size () > 0 ) {
//                for (Object[] objects : resultList) {
//
//                    System.out.println(Arrays.toString(objects));
//                }
//
//            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
