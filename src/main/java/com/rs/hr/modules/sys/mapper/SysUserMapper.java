package com.rs.hr.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.hr.modules.resume.entity.UserInviteCommission;
import com.rs.hr.modules.sys.entity.SysUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select m.perms from sys_user_role ur " +
            "   LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "   LEFT JOIN sys_menu m on rm.menu_id = m.id" +
            "   where ur.user_id = #{userId}")
    List<String> queryAllPerms(String userId);

    /**
     * 查询用户的所有菜单ID
     */
    @Select("select distinct rm.menu_id from sys_user_role ur " +
            "   LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "   where ur.user_id = #{userId}")
    List<String> queryAllMenuId(String userId);
}
