package com.example.mytabhost;
import java.io.Serializable;
public class NotificationEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer type;// "路况播报", "公示公告", "道路信息","出行提示"
	private String date;//公告发布时间
	private String content;//公告具体内容（HTML格式）
	private String title;//公告标题

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
