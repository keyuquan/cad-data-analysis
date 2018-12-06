package com.cad.data.database.mysql.utils;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.flume.Context;

import javax.sql.DataSource;
import java.util.ResourceBundle;

/**
 * Created by jh on 2017/9/8.
 */
public class JDBCTools {

    // 配置文件的数据库连接池
    private static BasicDataSource datasource_ibd = new BasicDataSource ();

    // 湖北的数据库数据库连接池
    private static BasicDataSource datasource_data = new BasicDataSource ();

    // 配置文件的 QueryRunner
    private static QueryRunner queryrunner_data = null;

    // 湖北的数据库 QueryRunner
    private static QueryRunner queryrunner_ibd = null;

    private static ResourceBundle configBundle = ResourceBundle.getBundle ( "source-config" );

    static {
        initDatasource ( datasource_ibd, configBundle.getString ( "mysql.ibd.url" ), configBundle.getString ( "mysql.ibd.username" ), configBundle.getString ( "mysql.ibd.password" ), 6, 2 );
    }

    public static DataSource getIbdDataSource() {
        return datasource_ibd;
    }

    public static QueryRunner getIbdQueryRunner() {
        queryrunner_ibd = new QueryRunner ( datasource_ibd );
        return queryrunner_ibd;
    }

    public static DataSource getDataSource(Context context) {

        initDatasource ( datasource_data, context.getString ( "mysql.data.url" ), context.getString ( "mysql.data.username" ), context.getString ( "mysql.data.password" ), 10, 5 );

        return datasource_data;
    }

    public static QueryRunner getDataQueryRunner(Context context) {
        if ( queryrunner_data == null ) {
            DataSource thisDataSource = getDataSource ( context );
            queryrunner_data = new QueryRunner ( thisDataSource );
        }
        return queryrunner_data;
    }

    private static void initDatasource(BasicDataSource datasource_data, String string, String string2, String string3, int i, int i2) {
        datasource_data.setDriverClassName ( configBundle.getString ( "mysql.driver.class" ) );
        datasource_data.setUrl ( string );
        datasource_data.setUsername ( string2 );
        datasource_data.setPassword ( string3 );
        datasource_data.setPoolPreparedStatements ( true );
        datasource_data.setInitialSize ( 5 );//初始化的连接数
        datasource_data.setMaxActive ( i );//最大连接数量
        datasource_data.setMaxIdle ( i2 );//最大空闲数
        datasource_data.setMinIdle ( 1 );//最小空闲
        datasource_data.setTimeBetweenEvictionRunsMillis ( 6000L );
        datasource_data.setMinEvictableIdleTimeMillis ( 300000L );
        datasource_data.setValidationQuery ( "SELECT 1 FROM DUAL" );
    }


    public static void main(String[] args) {
        getIbdDataSource ();
    }

}
