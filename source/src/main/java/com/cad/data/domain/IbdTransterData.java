package com.cad.data.domain;

import java.io.Serializable;
import java.util.Date;

public class IbdTransterData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 系统code（lm 或者 jg）
     */
    private String AreaType;

    /**
     * 数据来源（mysql, es, mongo）
     */
    private String dataSource;

    /**
     * 业务类型（注册，行为，商业...）
     */
    private String LogType;

    /**
     * 查询数据来源的表名（索引名，文档名）
     */
    private String tableName;

    /**
     * 查询条件（开始时间）
     */
    private String conditionStime;

    /**
     * 查询条件（结束时间）
     */
    private String conditionEtime;

    /**
     * 导入的记录数
     */
    private Long dataCount;

    /**
     * 状态（0:进入任务，1:导入成功，-1:导入失败）
     */
    private Integer status;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 重试次数 , 默认值为0
     */
    private Integer retry;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public IbdTransterData() {
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaType() {
        return AreaType;
    }

    public void setAreaType(String areaType) {
        AreaType = areaType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getLogType() {
        return LogType;
    }

    public void setLogType(String logType) {
        LogType = logType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getConditionStime() {
        return conditionStime;
    }

    public void setConditionStime(String conditionStime) {
        this.conditionStime = conditionStime;
    }

    public String getConditionEtime() {
        return conditionEtime;
    }

    public void setConditionEtime(String conditionEtime) {
        this.conditionEtime = conditionEtime;
    }
}