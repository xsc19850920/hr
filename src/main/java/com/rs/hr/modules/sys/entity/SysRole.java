package com.rs.hr.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sxia
 * @Description: TODO(角色)
 * @date 2017-6-23 15:07
 */
public class SysRole implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	private String id;

	/**
	 * 角色名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建者ID
	 */
//	private String createUserId;
	@TableField(exist = false)
	private List<String> menuIdList;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

//	public String getCreateUserId() {
//		return createUserId;
//	}
//
//	public void setCreateUserId(String createUserId) {
//		this.createUserId = createUserId;
//	}

	public List<String> getMenuIdList() {
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		this.menuIdList = menuIdList;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	
	
	
}
