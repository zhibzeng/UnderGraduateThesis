package com.example.mytabhost;
import java.io.Serializable;
public class NotificationEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer type;// "·������", "��ʾ����", "��·��Ϣ","������ʾ"
	private String date;//���淢��ʱ��
	private String content;//����������ݣ�HTML��ʽ��
	private String title;//�������

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
