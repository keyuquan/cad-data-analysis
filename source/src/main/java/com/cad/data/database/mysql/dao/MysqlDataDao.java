package com.cad.data.database.mysql.dao;

import com.cad.data.database.mysql.utils.JDBCTools;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import java.util.List;
import java.util.Map;

public class MysqlDataDao {

    /**
     * 查询 MySQL 数据
     */
    public static  List<Map<String, Object>>  getMysqlData(Context flumeCtx, String stime, String etime) {

        if ( StringUtils.isNotEmpty ( stime ) ) {
            try {
                String sqlStr = flumeCtx.getString ( "mysql.data.sql" );
                sqlStr = sqlStr.replace ( "${start_time}", stime ).replace ( "${end_time}", etime );
                List<Map<String, Object>> resultList = JDBCTools.getIbdQueryRunner ().query ( sqlStr, new MapListHandler () );

                return resultList;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return null;
    }

}
