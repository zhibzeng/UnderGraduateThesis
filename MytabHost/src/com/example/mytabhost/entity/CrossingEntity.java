package com.example.mytabhost.entity;

import java.io.Serializable;

/**
 * @Class 二环路口实体类 
 * @author JeffreyTseng
 *
 */
public class CrossingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int type; // 属于内环还是外环 0 表示内环 ；1 表示外环
	private String longitude;
	private String latitude;
	private int labelid; // 路口类型 出口入口，形状等等
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getLabelid() {
		return labelid;
	}
	public void setLabelid(int labelid) {
		this.labelid = labelid;
	}
	

}
