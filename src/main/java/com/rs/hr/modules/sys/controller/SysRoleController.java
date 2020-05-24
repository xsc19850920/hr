package com.rs.hr.modules.sys.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.hr.common.PageUtils;
import com.rs.hr.common.Result;
import com.rs.hr.common.annotation.SysLog;
import com.rs.hr.modules.sys.entity.SysRole;
import com.rs.hr.modules.sys.entity.SysRoleMenu;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.service.SysRoleMenuService;
import com.rs.hr.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sxia
 * @Description: TODO(角色管理)
 * @date 2017-6-23 15:07
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Page<SysRole> page =  sysRoleService.page(new Page<>(NumberUtil.parseLong(params.get("page").toString()),NumberUtil.parseLong(params.get("limit").toString())),new QueryWrapper<SysRole>().allEq(ignoreParam(params)));
		PageUtils pageUtil = new PageUtils(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
		return Result.ok().put("page", pageUtil);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Result select(){
		List<SysRole> list = sysRoleService.list();
		return Result.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public Result info(@PathVariable("roleId") String roleId){
		SysRole role = sysRoleService.getById(roleId);
		//查询角色对应的菜单
		List<String> menuIdList = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId)).stream().map(item->item.getMenuId()).collect(Collectors.toList());
		role.setMenuIdList(menuIdList);
		return Result.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody SysRole role){
		if(ObjectUtil.isEmpty(role) || StrUtil.isEmpty(role.getName())){
			return Result.error("参数不正确");
		}
		sysRoleService.save(role);
		return Result.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody SysRole role){
		if(ObjectUtil.isEmpty(role) || StrUtil.isEmpty(role.getName())|| StrUtil.isEmpty(role.getId())){
			return Result.error("参数不正确");
		}
		sysRoleService.updateById(role);
		return Result.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody String[] roleIds){
		sysRoleService.removeByIds(Arrays.asList(roleIds));
		return Result.ok();
	}
}
