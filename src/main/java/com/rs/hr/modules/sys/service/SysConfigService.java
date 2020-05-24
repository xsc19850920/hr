package com.rs.hr.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.common.RedisUtils;
import com.rs.hr.common.SQLFilter;
import com.rs.hr.modules.sys.entity.SysConfig;
import com.rs.hr.modules.sys.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> implements IService<SysConfig> {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SysConfigMapper sysConfigMapper;

    public SysConfig getBykey(String key){
        key = SQLFilter.sqlInject(key);
        SysConfig sysConfig = redisUtils.get(Constant.REDIS_SYS_CONFIG+ key,SysConfig.class);
        if(sysConfig == null){
            sysConfig = sysConfigMapper.selectOne(new QueryWrapper<SysConfig>().eq("config_key",key));
            if(sysConfig != null){
                String id=Constant.REDIS_SYS_CONFIG+sysConfig.getId();
                redisUtils.set(id, sysConfig);

                String configKey=Constant.REDIS_SYS_CONFIG+sysConfig.getConfigKey();
                redisUtils.set(configKey, sysConfig);
            }

        }
        return sysConfig;
    }
}
