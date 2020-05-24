package com.rs.hr.common.annotation;

import cn.hutool.json.JSONUtil;
import com.rs.hr.common.HttpContextUtils;
import com.rs.hr.common.IPUtils;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.service.SysLogService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author sxia
 * @Description: TODO(系统日志，切面处理类)
 * @date 2017-6-23 15:07
 */
@Aspect
@Component
public class SysLogAspect {

	@Autowired
	private SysLogService sysLogService;
	
	@Pointcut("@annotation(com.rs.hr.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		com.rs.hr.modules.sys.entity.SysLog sysLog = new com.rs.hr.modules.sys.entity.SysLog();
		SysLog logAnnotation = method.getAnnotation(SysLog.class);
		if(logAnnotation != null){
			//注解上的描述
			sysLog.setOperation(logAnnotation.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = JSONUtil.toJsonStr(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username = ((SysUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
	
}
