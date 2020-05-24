package com.rs.hr.modules.sys.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.hr.common.PageUtils;
import com.rs.hr.common.Result;
import com.rs.hr.common.annotation.SysLog;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.entity.SysUserRole;
import com.rs.hr.modules.sys.service.SysUserRoleService;
import com.rs.hr.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sxia
 * @Description: TODO(系统用户)
 * @date 2017-6-23 15:07
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据

		Page<SysUser> page =  sysUserService.page(new Page<>(NumberUtil.parseLong(params.get("page").toString()),NumberUtil.parseLong(params.get("limit").toString())),new QueryWrapper<SysUser>().allEq(ignoreParam(params)));
        PageUtils pageUtil = new PageUtils(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());

		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public Result info(){
		return Result.ok().put("user", getUser());
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/updatePassword")
	public Result updatePassword(String password, String newPassword){
		if(StrUtil.isBlank(newPassword)){
			return Result.error("新密码不为能空");
		}

		//sha256加密
		password = new Sha256Hash(password, getUser().getSalt()).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();

		//更新密码
        SysUser currentUser = getUser();
        currentUser.setPassword(newPassword);
		if(!sysUserService.updateById(currentUser)){
			return Result.error("原密码不正确");
		}

		return Result.ok();
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public Result info(@PathVariable("userId") String userId){
		SysUser user = sysUserService.getById(userId);

		//获取用户所属的角色列表
		List<String> roleIdList = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("user_id",userId)).stream().map(item->item.getRoleId()).collect(Collectors.toList());
		user.setRoleIdList(roleIdList);

		return Result.ok().put("user", user);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody SysUser user){
		if(StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword())){
			return Result.error("用户名或密码不能为空");
		}
		sysUserService.save(user);
		return Result.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody SysUser user){
		if(StrUtil.isEmpty(user.getUsername()) || StrUtil.isEmpty(user.getPassword())){
			return Result.error("用户名或密码不能为空");
		}
		sysUserService.updateById(user);
		return Result.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody String[] userIds){
		if(ArrayUtil.contains(userIds, 1L)){
			return Result.error("系统管理员不能删除");
		}
		if(ArrayUtil.contains(userIds, getUserId())){
			return Result.error("当前用户不能删除");
		}
		sysUserService.removeByIds(Arrays.asList(userIds));
		return Result.ok();
	}
}
