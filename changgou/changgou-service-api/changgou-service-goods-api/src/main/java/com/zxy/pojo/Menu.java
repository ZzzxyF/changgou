package com.zxy.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:admin
 * @Description:Menu构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_menu")
public class Menu implements Serializable{

	@Id
    @Column(name = "id")
	private String id;//菜单ID

    @Column(name = "name")
	private String name;//菜单名称

    @Column(name = "icon")
	private String icon;//图标

    @Column(name = "url")
	private String url;//URL

    @Column(name = "parent_id")
	private String parentId;//上级菜单ID



	//get方法
	public String getId() {
		return id;
	}

	//set方法
	public void setId(String id) {
		this.id = id;
	}
	//get方法
	public String getName() {
		return name;
	}

	//set方法
	public void setName(String name) {
		this.name = name;
	}
	//get方法
	public String getIcon() {
		return icon;
	}

	//set方法
	public void setIcon(String icon) {
		this.icon = icon;
	}
	//get方法
	public String getUrl() {
		return url;
	}

	//set方法
	public void setUrl(String url) {
		this.url = url;
	}
	//get方法
	public String getParentId() {
		return parentId;
	}

	//set方法
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


}
