package com.rs.hr.modules.sys.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.hr.common.PageUtils;
import com.rs.hr.common.Result;
import com.rs.hr.modules.sys.entity.SysLog;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author sxia
 * @Description: TODO(系统日志)
 * @date 2017-6-23 15:07
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends AbstractController{
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Page<SysLog> page =  sysLogService.page(new Page<>(NumberUtil.parseLong(params.get("page").toString()),NumberUtil.parseLong(params.get("limit").toString())),new QueryWrapper<SysLog>().allEq(ignoreParam(params)).orderByDesc("create_time"));
		PageUtils pageUtil = new PageUtils(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
		return Result.ok().put("page", pageUtil);
	}
	
	/**
	 * 附件信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:log:info")
	public Result info(@PathVariable("id") Long id) {
		 SysLog log = sysLogService.getById(id);
		return Result.ok().put("log", log);
	}
	
}
