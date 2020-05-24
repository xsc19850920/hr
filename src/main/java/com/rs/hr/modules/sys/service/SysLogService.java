package com.rs.hr.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.modules.sys.entity.SysLog;
import com.rs.hr.modules.sys.entity.SysMenu;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.mapper.SysLogMapper;
import com.rs.hr.modules.sys.mapper.SysMenuMapper;
import com.rs.hr.modules.sys.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysLogService extends ServiceImpl<SysLogMapper, SysLog> implements IService<SysLog> {

}
