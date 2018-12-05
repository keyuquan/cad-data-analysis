package com.cad.data.database.mysql.dao;

import com.cad.data.database.mysql.utils.JDBCTools;
import com.cad.data.domain.IbdTransterData;
import com.cad.data.enums.DataStatusEnum;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IbdTransterDataDao {

    private static QueryRunner queryRunner = new QueryRunner ( JDBCTools.getIbdDataSource () );

    /**
     * 获取导数据记录中，“最晚的”导入时间（断点续传）
     *
     * @param ibdTransterData
     * @return
     * @throws Exception
     */
    public static String getLatestTransferTime(IbdTransterData ibdTransterData) throws Exception {
        if ( ibdTransterData != null && StringUtils.isNotEmpty ( ibdTransterData.getAreaType () ) ) {
            String sqlStr = "SELECT condition_etime as conditionEtime FROM ibd_transter_data WHERE area_type = '" + ibdTransterData.getAreaType () + "' ";
            if ( StringUtils.isNotEmpty ( ibdTransterData.getDataSource () ) ) {
                sqlStr += "AND data_source = '" + ibdTransterData.getDataSource () + "' ";
            }
            if ( StringUtils.isNotEmpty ( ibdTransterData.getLogType () ) ) {
                sqlStr += "AND log_type = '" + ibdTransterData.getLogType () + "' ";
            }
            sqlStr += "ORDER BY condition_etime DESC LIMIT 1 ";
            List<IbdTransterData> dataList = queryRunner.query ( sqlStr, new BeanListHandler<> ( IbdTransterData.class ) );
            if ( dataList != null && dataList.size () > 0 ) {
                return dataList.get ( 0 ).getConditionEtime ();
            }
        }
        return null;
    }

    /**
     * 新增和更新记录
     *
     * @param ibdTransterData
     * @return
     */
    public static String addIbdTransterData(IbdTransterData ibdTransterData) {
        try {
            // queryRunner.insert(sql, rsh)
            String sql = "insert into ibd_transter_data (id, area_type, data_source, log_type, table_name, condition_stime, condition_etime, data_count, status, remark, retry) values(?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE table_name=VALUES(table_name), condition_stime=VALUES(condition_stime),condition_etime=VALUES(condition_etime)";
            List<Object> paramList = new ArrayList<Object> ();
            String thisId = UUID.randomUUID ().toString ().replace ( "-", "" );
            paramList.add ( thisId );
            paramList.add ( ibdTransterData.getAreaType () );
            paramList.add ( ibdTransterData.getDataSource () );
            paramList.add ( ibdTransterData.getLogType () );
            paramList.add ( ibdTransterData.getTableName () );
            paramList.add ( ibdTransterData.getConditionStime () );
            paramList.add ( ibdTransterData.getConditionEtime () );
            paramList.add ( ibdTransterData.getDataCount () );
            paramList.add ( DataStatusEnum.INIT.getCode () );
            paramList.add ( ibdTransterData.getRemark () );
            paramList.add ( 0 );

            queryRunner.update ( sql, paramList.toArray () );
            return thisId;
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * 修改记录
     * 主要是修改： data_count , status , remark , update_time 这几个字段
     *
     * @param ibdTransterData
     */
    public static void updateIbdTransterDataById(IbdTransterData ibdTransterData) {
        if ( StringUtils.isNotEmpty ( ibdTransterData.getId () ) ) {
            try {
                // UPDATE ibd_transter_data SET data_count = 100 , remark = 'abc' , update_time = '2018-07-09 14:22:25' WHERE id = '0c4090413814445193dd299cb2af6816'
                String sql = "UPDATE ibd_transter_data SET ";
                if ( ibdTransterData.getDataCount () != null ) {
                    sql += " data_count = " + ibdTransterData.getDataCount () + ", ";
                }
                if ( ibdTransterData.getStatus () != null ) {
                    sql += " status = " + ibdTransterData.getStatus () + ", ";
                }
                if ( StringUtils.isNotEmpty ( ibdTransterData.getRemark () ) ) {
                    sql += " remark = '" + ibdTransterData.getRemark () + "', ";
                }
                if ( ibdTransterData.getRetry () != null && ibdTransterData.getRetry () > 0 ) {
                    sql += " retry = " + ibdTransterData.getRetry () + ", ";
                }
                // 添加修改时间，和id的查询条件
                DateFormat timeDf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
                sql += " update_time = '" + timeDf.format ( new Date () ) + "' WHERE id = '" + ibdTransterData.getId () + "'";
                queryRunner.update ( sql );
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        }
    }

    /**
     * 根据业务类型查询, 获取需要重试（最多3次）的错误记录数据
     *
     * @param stime
     * @param etime
     * @return
     */
    public static List<IbdTransterData> getExceptionRetryDataByLogType(String areaType, String logType, String stime, String etime) {
        try {
            String showCols = "id, retry, system_code AS 'systemCode', data_source AS 'dataSource', log_type AS 'logType', table_name AS 'tableName', condition_stime AS 'conditionStime', condition_etime AS 'conditionEtime', status AS 'status', create_time AS 'createTime'";
            String sql = "SELECT " + showCols + " FROM ibd_transter_data WHERE status <= 0 AND retry < 3 ";
            if ( StringUtils.isNotEmpty ( areaType ) ) {
                sql += "AND area_type = '" + areaType + "' ";
            }
            if ( StringUtils.isNotEmpty ( logType ) ) {
                sql += "AND log_type = '" + logType + "' ";
            }
            if ( StringUtils.isNotEmpty ( stime ) ) {
                sql += "AND create_time >= '" + stime + "' ";
            }
            if ( StringUtils.isNotEmpty ( etime ) ) {
                sql += "AND create_time <= '" + etime + "' ";
            }
            List<IbdTransterData> dataList = queryRunner.query ( sql, new BeanListHandler<> ( IbdTransterData.class ) );
            if ( dataList != null && dataList.size () > 0 ) {
                return dataList;
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    
}
