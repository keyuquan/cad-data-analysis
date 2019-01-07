package com.cad.data.domain;

import java.io.Serializable;

//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;

/**
 * Created by chenxiaogang on 18/6/4 0004.
 * version: 0.1
 */
public class BaseDomain implements Serializable{

	private static final long serialVersionUID = 1L;
	
//	@NotNull
//	@Pattern(regexp="^lm$|^jg$", message="字段只能传lm或者jg值")
    private String origin_system;
//	@NotNull
    private String business_type;
//	@NotNull
    private Integer user_id;
//	@NotNull
    private String user_account;

    private String app_channel;
//    @NotNull
    private String app_version;
//    @NotNull
    private String platform;
//    @NotNull
    private String device_system;
//    @NotNull
    private String device_identifier;
    
	public String getOrigin_system() {
		return origin_system;
	}
	public void setOrigin_system(String origin_system) {
		this.origin_system = origin_system;
	}
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getApp_channel() {
		return app_channel;
	}
	public void setApp_channel(String app_channel) {
		this.app_channel = app_channel;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDevice_system() {
		return device_system;
	}
	public void setDevice_system(String device_system) {
		this.device_system = device_system;
	}
	public String getDevice_identifier() {
		return device_identifier;
	}
	public void setDevice_identifier(String device_identifier) {
		this.device_identifier = device_identifier;
	}

}
