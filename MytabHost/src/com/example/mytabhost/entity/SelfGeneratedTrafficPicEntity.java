package com.example.mytabhost.entity;

import java.io.Serializable;

public class SelfGeneratedTrafficPicEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String datetime;//����ʱ��
	private String Longitude;//����
	private String Latitude;//γ��
	private String notes;//�¼�����
	private String picPath;//ͼƬ·��
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
