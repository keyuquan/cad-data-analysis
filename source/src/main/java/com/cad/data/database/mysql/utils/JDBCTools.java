package com.cad.data.database.mysql.utils;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;

import javax.sql.DataSource;
import java.util.ResourceBundle;

/**
 * Created by jh on 2017/9/8.
 */
public class JDBCTools {

    private static BasicDataSource datasource_ibd = new BasicDataSource ();

    private static BasicDataSource datasource_hb = new BasicDataSource ();

    private static QueryRunner HbQueryRunner = null;

    private static ResourceBundle configBundle = ResourceBundle.getBundle ( "source-config" );

    static {
        datasource_ibd.setDriverClassName ( configBundle.getString ( "mysql.driver.class" ) );
        datasource_ibd.setUrl ( configBundle.getString ( "mysql.ibd.url" ) );
        datasource_ibd.setUsername ( configBundle.getString ( "mysql.ibd.username" ) );
        datasource_ibd.setPassword ( configBundle.getString ( "mysql.ibd.password" ) );
        datasource_ibd.setPoolPreparedStatements ( true );
        datasource_ibd.setInitialSize ( 5 );//初始化的连接数
        datasource_ibd.setMaxActive ( 6 );//最大连接数量
        datasource_ibd.setMaxIdle ( 2 );//最大空闲数
        datasource_ibd.setMinIdle ( 1 );//最小空闲
        datasource_ibd.setTimeBetweenEvictionRunsMillis ( 6000L );
        datasource_ibd.setMinEvictableIdleTimeMillis ( 300000L );
        datasource_ibd.setValidationQuery ( "SELECT 1 FROM DUAL" );
    }

    public static DataSource getIbdDataSource() {
        return datasource_ibd;
    }

    public static DataSource getHbDataSource(Context context) {

        if ( StringUtils.isEmpty ( datasource_hb.getUrl () ) ) {
            if ( context != null && StringUtils.isNotEmpty ( context.getString ( "mysql.hb.url" ).trim () ) ) {
                datasource_hb.setDriverClassName ( configBundle.getString ( "mysql.driver.class" ) );
                datasource_hb.setUrl ( context.getString ( "mysql.hb.url" ) );
                datasource_hb.setUsername ( context.getString ( "mysql.hb.username" ) );
                datasource_hb.setPassword ( context.getString ( "mysql.hb.password" ) );
                datasource_hb.setPoolPreparedStatements ( true );
                datasource_hb.setInitialSize ( 5 );//初始化的连接数
                datasource_hb.setMaxActive ( 10 );//最大连接数量
                datasource_hb.setMaxIdle ( 5 );//最大空闲数
                datasource_hb.setMinIdle ( 1 );//最小空闲
                datasource_hb.setTimeBetweenEvictionRunsMillis ( 6000L );
                datasource_hb.setMinEvictableIdleTimeMillis ( 300000L );
                datasource_hb.setValidationQuery ( "SELECT 1 FROM DUAL" );
            } else {
                //数据库连接信息,必须的
                datasource_hb.setDriverClassName ( configBundle.getString ( "mysql.driver.class" ) );
                datasource_hb.setUrl ( configBundle.getString ( "mysql.hb.url" ) );
                datasource_hb.setUsername ( configBundle.getString ( "mysql.hb.username" ) );
                datasource_hb.setPassword ( configBundle.getString ( "mysql.hb.password" ) );
                datasource_hb.setPoolPreparedStatements ( true );
                //对象连接池中的连接数量配置,可选的
                datasource_hb.setInitialSize ( 5 );//初始化的连接数
                datasource_hb.setMaxActive ( 10 );//最大连接数量
                datasource_hb.setMaxIdle ( 5 );//最大空闲数
                datasource_hb.setMinIdle ( 1 );//最小空闲
                datasource_hb.setTimeBetweenEvictionRunsMillis ( 6000L );
                datasource_hb.setMinEvictableIdleTimeMillis ( 300000L );
                datasource_hb.setValidationQuery ( "SELECT 1 FROM DUAL" );
            }
        }
        return datasource_hb;
    }

    public static QueryRunner getHbQueryRunner(Context context) {
        if ( HbQueryRunner == null ) {
            DataSource thisDataSource = getHbDataSource ( context );
            HbQueryRunner = new QueryRunner ( thisDataSource );
        }
        return HbQueryRunner;
    }

    public static void main(String[] args) {
        getIbdDataSource ();
    }

}
