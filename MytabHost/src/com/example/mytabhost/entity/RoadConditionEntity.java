package com.example.mytabhost.entity;

import java.io.Serializable;

public class RoadConditionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String content;
	private String datetime;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
