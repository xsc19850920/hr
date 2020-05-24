package com.rs.hr.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.modules.sys.entity.SysMenu;
import com.rs.hr.modules.sys.mapper.SysMenuMapper;
import com.rs.hr.modules.sys.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> implements IService<SysMenu> {
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    public List<SysMenu> getUserMenuList(String userId) {
        //系统管理员，拥有最高权限
        if(userId.equals(Constant.SUPER_ADMIN)){
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<String> menuIdList = sysUserMapper.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenu> getAllMenuList(List<String> menuIdList){
        //查询根菜单列表
        List<SysMenu> menuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id","0").orderByAsc("order_num"));
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }


    /**
     * 递归
     */
    private List<SysMenu> getMenuTreeList(List<SysMenu> menuList, List<String> menuIdList){
        List<SysMenu> subMenuList = new ArrayList<SysMenu>();

        for(SysMenu entity : menuList){
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){//目录
                entity.setList(getMenuTreeList(sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id",entity.getId()).orderByAsc("order_num")), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

}
