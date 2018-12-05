package com.cad.data.threads;

import com.cad.data.database.SqlMapUtils;
import com.cad.data.domain.AlarmeventLog;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class Test {
    private static BasicDataSource datasource_hb = new BasicDataSource ();
    private static QueryRunner HbQueryRunner = null;

    public static void main(String[] args) {

        try {

            datasource_hb.setDriverClassName ( "com.mysql.jdbc.Driver" );
            datasource_hb.setUrl ( "jdbc:mysql://59.175.148.195:12106/carlink?useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&allowMultiQueries=true" );
            datasource_hb.setUsername ( "carlinktest" );
            datasource_hb.setPassword ( "ahMai6EKifohxughaY" );
            datasource_hb.setPoolPreparedStatements ( true );
            datasource_hb.setInitialSize ( 5 );//初始化的连接数
            datasource_hb.setMaxActive ( 10 );//最大连接数量
            datasource_hb.setMaxIdle ( 5 );//最大空闲数
            datasource_hb.setMinIdle ( 1 );//最小空闲
            datasource_hb.setTimeBetweenEvictionRunsMillis ( 6000L );
            datasource_hb.setMinEvictableIdleTimeMillis ( 300000L );
            datasource_hb.setValidationQuery ( "SELECT 1 FROM DUAL" );
            HbQueryRunner = new QueryRunner ( datasource_hb );

            String sqlStr = SqlMapUtils.getLmSqlMap ().get ( "hb_alarmevent_log" );
            sqlStr = sqlStr.replace ( "${stime}", "2018-06-01 00:00:00" ).replace ( "${etime}", "2018-06-02 00:00:00" );
            System.out.println ( sqlStr );

            List<AlarmeventLog> resultList = HbQueryRunner.query ( sqlStr, new BeanListHandler<> ( AlarmeventLog.class ) );
            // 时间格式有问题，需要转换
            if ( resultList != null && resultList.size () > 0 ) {
                for (AlarmeventLog thisLogReq : resultList) {
                    System.out.println ( thisLogReq.toString () );

                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
