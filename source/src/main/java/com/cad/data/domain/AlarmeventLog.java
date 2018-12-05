package com.cad.data.domain;

import java.math.BigDecimal;
import java.util.Date;

public class AlarmeventLog extends BaseDomain {

    private Integer provinceId;
    private String provinceName;
    private Integer cityId;
    private String cityName;
    private Integer districtId;
    private String districtName;
    private Integer companyId;
    private String companyName;
    private String vehicleCode;
    private String plateNum;
    private String plateColorCode;
    private Integer vehicleOperateTypeId;
    private String vehicleOperateTypeCode;
    private String vehicleOperateTypeName;
    private String alarmTypeCode;
    private String driverwarningtype;
    private String alarmLevel;
    private String eventId;
    private String eventStartTime;
    private BigDecimal eventStartLongitude;
    private BigDecimal eventStartLatitude;
    private String eventEndTime;
    private BigDecimal eventEndLongitude;
    private BigDecimal eventEndLatitude;
    private Integer eventPersistSeconds;
    private String eventStatus;
    private String eventPicGroupName;
    private String eventPicRemoteFileName;
    private Integer maxSpeed;
    private Integer minSpeed;
    private Integer avgSpeed;
    private String eventDesc;
    private BigDecimal violationDistance;
    private Integer hasManual;
    private Integer status;
    private String readTime;
    private String readUserId;
    private String receiptTime;
    private String receiptUserId;
    private Integer hasUnReceiptMsg;
    private String createTime;
    private String createUserId;
    private String lastModifyTime;
    private String lastModifyUserId;
    private Integer valid;
    private String eventOriginalTime;
    private BigDecimal eventOriginalLongitude;
    private BigDecimal eventOriginalLatitude;
    private String eventStartDate;
    private String contrastPicRemoteFileName;
    private String contrastPicGroupName;
    private BigDecimal similarityRatio;
    private Integer limitSpeed;
    private Integer operatorId;
    private String operatorName;
    private Integer alarmconfig_id;
    private String lastReceiveTime;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getPlateColorCode() {
        return plateColorCode;
    }

    public void setPlateColorCode(String plateColorCode) {
        this.plateColorCode = plateColorCode;
    }

    public Integer getVehicleOperateTypeId() {
        return vehicleOperateTypeId;
    }

    public void setVehicleOperateTypeId(Integer vehicleOperateTypeId) {
        this.vehicleOperateTypeId = vehicleOperateTypeId;
    }

    public String getVehicleOperateTypeCode() {
        return vehicleOperateTypeCode;
    }

    public void setVehicleOperateTypeCode(String vehicleOperateTypeCode) {
        this.vehicleOperateTypeCode = vehicleOperateTypeCode;
    }

    public String getVehicleOperateTypeName() {
        return vehicleOperateTypeName;
    }

    public void setVehicleOperateTypeName(String vehicleOperateTypeName) {
        this.vehicleOperateTypeName = vehicleOperateTypeName;
    }

    public String getAlarmTypeCode() {
        return alarmTypeCode;
    }

    public void setAlarmTypeCode(String alarmTypeCode) {
        this.alarmTypeCode = alarmTypeCode;
    }

    public String getDriverwarningtype() {
        return driverwarningtype;
    }

    public void setDriverwarningtype(String driverwarningtype) {
        this.driverwarningtype = driverwarningtype;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public BigDecimal getEventStartLongitude() {
        return eventStartLongitude;
    }

    public void setEventStartLongitude(BigDecimal eventStartLongitude) {
        this.eventStartLongitude = eventStartLongitude;
    }

    public BigDecimal getEventStartLatitude() {
        return eventStartLatitude;
    }

    public void setEventStartLatitude(BigDecimal eventStartLatitude) {
        this.eventStartLatitude = eventStartLatitude;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public BigDecimal getEventEndLongitude() {
        return eventEndLongitude;
    }

    public void setEventEndLongitude(BigDecimal eventEndLongitude) {
        this.eventEndLongitude = eventEndLongitude;
    }

    public BigDecimal getEventEndLatitude() {
        return eventEndLatitude;
    }

    public void setEventEndLatitude(BigDecimal eventEndLatitude) {
        this.eventEndLatitude = eventEndLatitude;
    }

    public Integer getEventPersistSeconds() {
        return eventPersistSeconds;
    }

    public void setEventPersistSeconds(Integer eventPersistSeconds) {
        this.eventPersistSeconds = eventPersistSeconds;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventPicGroupName() {
        return eventPicGroupName;
    }

    public void setEventPicGroupName(String eventPicGroupName) {
        this.eventPicGroupName = eventPicGroupName;
    }

    public String getEventPicRemoteFileName() {
        return eventPicRemoteFileName;
    }

    public void setEventPicRemoteFileName(String eventPicRemoteFileName) {
        this.eventPicRemoteFileName = eventPicRemoteFileName;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Integer minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Integer getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Integer avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public BigDecimal getViolationDistance() {
        return violationDistance;
    }

    public void setViolationDistance(BigDecimal violationDistance) {
        this.violationDistance = violationDistance;
    }

    public Integer getHasManual() {
        return hasManual;
    }

    public void setHasManual(Integer hasManual) {
        this.hasManual = hasManual;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getReadUserId() {
        return readUserId;
    }

    public void setReadUserId(String readUserId) {
        this.readUserId = readUserId;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getReceiptUserId() {
        return receiptUserId;
    }

    public void setReceiptUserId(String receiptUserId) {
        this.receiptUserId = receiptUserId;
    }

    public Integer getHasUnReceiptMsg() {
        return hasUnReceiptMsg;
    }

    public void setHasUnReceiptMsg(Integer hasUnReceiptMsg) {
        this.hasUnReceiptMsg = hasUnReceiptMsg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getEventOriginalTime() {
        return eventOriginalTime;
    }

    public void setEventOriginalTime(String eventOriginalTime) {
        this.eventOriginalTime = eventOriginalTime;
    }

    public BigDecimal getEventOriginalLongitude() {
        return eventOriginalLongitude;
    }

    public void setEventOriginalLongitude(BigDecimal eventOriginalLongitude) {
        this.eventOriginalLongitude = eventOriginalLongitude;
    }

    public BigDecimal getEventOriginalLatitude() {
        return eventOriginalLatitude;
    }

    public void setEventOriginalLatitude(BigDecimal eventOriginalLatitude) {
        this.eventOriginalLatitude = eventOriginalLatitude;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getContrastPicRemoteFileName() {
        return contrastPicRemoteFileName;
    }

    public void setContrastPicRemoteFileName(String contrastPicRemoteFileName) {
        this.contrastPicRemoteFileName = contrastPicRemoteFileName;
    }

    public String getContrastPicGroupName() {
        return contrastPicGroupName;
    }

    public void setContrastPicGroupName(String contrastPicGroupName) {
        this.contrastPicGroupName = contrastPicGroupName;
    }

    public BigDecimal getSimilarityRatio() {
        return similarityRatio;
    }

    public void setSimilarityRatio(BigDecimal similarityRatio) {
        this.similarityRatio = similarityRatio;
    }

    public Integer getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(Integer limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getAlarmconfig_id() {
        return alarmconfig_id;
    }

    public void setAlarmconfig_id(Integer alarmconfig_id) {
        this.alarmconfig_id = alarmconfig_id;
    }

    public String getLastReceiveTime() {
        return lastReceiveTime;
    }

    public void setLastReceiveTime(String lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }
}
