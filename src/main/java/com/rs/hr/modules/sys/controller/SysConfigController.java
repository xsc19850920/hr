package com.rs.hr.modules.sys.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.hr.common.Constant;
import com.rs.hr.common.PageUtils;
import com.rs.hr.common.Result;
import com.rs.hr.common.annotation.SysLog;
import com.rs.hr.modules.sys.entity.SysConfig;
import com.rs.hr.modules.sys.service.SysConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author sxia
 * @Description: TODO(系统参数信息)
 * @date 2017-6-23 15:07
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有参数列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:config:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Page<SysConfig> page =  sysConfigService.page(new Page<>(NumberUtil.parseLong(params.get("page").toString()),NumberUtil.parseLong(params.get("limit").toString())),new QueryWrapper<SysConfig>().allEq(ignoreParam(params)));
		PageUtils pageUtil = new PageUtils(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());

		return Result.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 参数信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public Result info(@PathVariable("id") Long id){
		SysConfig config = sysConfigService.getById(id);
		return Result.ok().put("profiles", config);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存配置")
	@RequestMapping("/save")
	@RequiresPermissions("sys:config:save")
	public Result save(@RequestBody SysConfig config){
		Result result = verifyForm(config);
		if(StrUtil.equals(result.get("code").toString(),"0")) {
			sysConfigService.save(config);
		}
		return result;
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改配置")
	@RequestMapping("/update")
	@RequiresPermissions("sys:config:update")
	public Result update(@RequestBody SysConfig config){
		Result result = verifyForm(config);
		if(StrUtil.equals(result.get("code").toString(),"0")) {
			sysConfigService.updateById(config);
		}
		return result;
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除配置")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public Result delete(@RequestBody Long[] ids){
		sysConfigService.removeByIds(Arrays.asList(ids));
		return Result.ok();
	}

	private Result verifyForm(SysConfig config){
		if(StrUtil.isBlank(config.getConfigKey())){
			return Result.error("参数名不能为空");
		}
		if(StrUtil.isBlank(config.getConfigValue())){
			return Result.error("参数值不能为空");
		}
		SysConfig configFromDb = sysConfigService.getBykey(config.getConfigKey());
		//修改
		if(config.getId()!=null) {
			if(configFromDb!=null && configFromDb.getId().compareTo(config.getId()) != 0){
				return Result.error("参数名已经存在");
			}
		}else{
			if (configFromDb != null && configFromDb.getConfigKey().equalsIgnoreCase(config.getConfigKey())) {
				return Result.error("参数名已经存在");
			}
		}
		return Result.ok();
	}

	@PostMapping("/importExcel")
	@RequiresPermissions("sys:config:upload")
	public Result importExcel(@RequestParam MultipartFile file) throws IOException {
		if(file.isEmpty()){
			return Result.error("导入文件异常");
		}
		String fileName = file.getOriginalFilename();
		String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		if(!StrUtil.equals(Constant.SUFFIX_EXCEL,suffixName)){
			return Result.error("导入文件类型错误");
		}

		ExcelReader reader = ExcelUtil.getReader(file.getInputStream(),0);
		List<SysConfig> list = reader.read(0, 1, SysConfig.class);
		return Result.ok();
	}

	@RequestMapping("/download")
	@RequiresPermissions("sys:config:download")
	public void download(HttpServletResponse response) throws IOException {
		response.setContentType("application/force-download");
		response.setHeader("Content-Disposition", "attachment;fileName=config.xls");
		String fileName = "config.xls";
		IoUtil.copy(ResourceUtil.getStreamSafe("templates/xls/"+fileName), response.getOutputStream(), IoUtil.DEFAULT_BUFFER_SIZE);
	}

}
