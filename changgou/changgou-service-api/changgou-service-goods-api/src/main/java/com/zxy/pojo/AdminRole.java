package com.zxy.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:admin
 * @Description:AdminRole构建
 * @Date 2019/6/14 19:13
 *****/
@Table(name="tb_admin_role")
public class AdminRole implements Serializable{

    @Column(name = "admin_id")
	private Integer adminId;//管理员ID

	@Id
    @Column(name = "role_id")
	private Integer roleId;//角色ID



	//get方法
	public Integer getAdminId() {
		return adminId;
	}

	//set方法
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	//get方法
	public Integer getRoleId() {
		return roleId;
	}

	//set方法
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


}
