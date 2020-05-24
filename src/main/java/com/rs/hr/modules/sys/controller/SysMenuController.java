package com.rs.hr.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rs.hr.common.Constant;
import com.rs.hr.common.Result;
import com.rs.hr.common.annotation.SysLog;
import com.rs.hr.modules.sys.entity.SysMenu;
import com.rs.hr.modules.sys.service.SysMenuService;
import com.rs.hr.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author sxia
 * @Description: TODO(系统菜单)
 * @date 2017-6-23 15:07
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 导航菜单
	 */
	@RequestMapping("/nav")
	public Result nav(){
		List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = sysUserService.getUserPermissions(getUserId());
		return Result.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenu> list(){
		List<SysMenu> menuList = sysMenuService.list();
		return menuList;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public Result select(){
		//查询列表数据
		List<SysMenu> menuList = sysMenuService.list(new QueryWrapper<SysMenu>().ne("type","2").orderByAsc("order_num"));

		//添加顶级菜单
		SysMenu root = new SysMenu();
		root.setId("0");
		root.setName("一级菜单");
		root.setParentId("-1");
		root.setOpen(true);
		menuList.add(root);

		return Result.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public Result info(@PathVariable("menuId") String menuId){
		SysMenu menu = sysMenuService.getById(menuId);
		return Result.ok().put("menu", menu);
	}

	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public Result save(@RequestBody SysMenu menu){
		//数据校验
		Result result = verifyForm(menu);
		if(StrUtil.equals(result.get("code").toString(),"0")){
			sysMenuService.save(menu);
		}
		return result;
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public Result update(@RequestBody SysMenu menu){
		//数据校验
		Result result = verifyForm(menu);
		if(StrUtil.equals(result.get("code").toString(),"0")){
			sysMenuService.updateById(menu);
		}
		return Result.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public Result delete(String menuId){
		//判断是否有子菜单或按钮
		List<SysMenu> menuList = sysMenuService.list(new QueryWrapper<SysMenu>().eq("parent_id",menuId).orderByAsc("order_num"));
		if(menuList.size() > 0){
			return Result.error("请先删除子菜单或按钮");
		}

		sysMenuService.removeById(menuId);

		return Result.ok();
	}

	/**
	 * 验证参数是否正确
	 */
	private Result verifyForm(SysMenu menu){
		if(StrUtil.isBlank(menu.getName())){
			return Result.error("菜单名称不能为空");
		}

		if(menu.getParentId() == null){
			return Result.error("上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StrUtil.isBlank(menu.getUrl())){
				return Result.error("菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(!StrUtil.equals(menu.getParentId(), "0")){
			SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				return Result.error("上级菜单只能为目录类型");
			}
		}

		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				return Result.error("上级菜单只能为菜单类型");
			}
		}
		return Result.ok();
	}
}
