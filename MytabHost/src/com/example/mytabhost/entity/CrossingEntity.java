package com.example.mytabhost.entity;

import java.io.Serializable;

/**
 * @Class ����·��ʵ���� 
 * @author JeffreyTseng
 *
 */
public class CrossingEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int type; // �����ڻ������⻷ 0 ��ʾ�ڻ� ��1 ��ʾ�⻷
	private String longitude;
	private String latitude;
	private int labelid; // ·������ ������ڣ���״�ȵ�
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
