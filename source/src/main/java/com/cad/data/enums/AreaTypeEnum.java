package com.cad.data.enums;

public enum AreaTypeEnum {

	HB("hb","湖北省"),
    HN("hn","海南省");

	private String code;
    private String name;
    
    AreaTypeEnum(String code, String name){
    	this.code = code;
        this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
