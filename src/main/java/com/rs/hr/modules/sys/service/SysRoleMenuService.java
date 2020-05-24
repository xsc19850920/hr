package com.rs.hr.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.RedisUtils;
import com.rs.hr.modules.sys.entity.SysRoleMenu;
import com.rs.hr.modules.sys.mapper.SysRoleMapper;
import com.rs.hr.modules.sys.mapper.SysRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements IService<SysRoleMenu> {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

}
