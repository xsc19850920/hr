package com.rs.hr.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.modules.sys.entity.SysMenu;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.mapper.SysMenuMapper;
import com.rs.hr.modules.sys.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements IService<SysUser> {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    public Set<String> getUserPermissions(String userId) {
        List<String> permsList;
        //系统管理员，拥有最高权限
        if (userId.equals(Constant.SUPER_ADMIN)) {
            List<SysMenu> menuList = sysMenuMapper.selectList(null);
            permsList =  menuList.stream().map(item->item.getPerms()).collect(Collectors.toList());
        } else {
            permsList = sysUserMapper.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StrUtil.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    public SysUser queryByUserName(String username) {
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

}
