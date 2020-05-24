package com.rs.hr.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.modules.sys.entity.SysLog;
import com.rs.hr.modules.sys.entity.SysUserRole;
import com.rs.hr.modules.sys.mapper.SysLogMapper;
import com.rs.hr.modules.sys.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements IService<SysUserRole> {

}
