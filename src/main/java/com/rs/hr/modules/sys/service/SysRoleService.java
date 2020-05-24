package com.rs.hr.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.common.RedisUtils;
import com.rs.hr.common.SQLFilter;
import com.rs.hr.modules.sys.entity.SysConfig;
import com.rs.hr.modules.sys.entity.SysRole;
import com.rs.hr.modules.sys.mapper.SysConfigMapper;
import com.rs.hr.modules.sys.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> implements IService<SysRole> {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysRoleMapper sysRoleMapper;

}
