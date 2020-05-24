package com.rs.hr.modules.sys.service;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.common.RedisUtils;
import com.rs.hr.modules.sys.entity.SysUserToken;
import com.rs.hr.modules.sys.mapper.SysUserTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserTokenService extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements IService<SysUserToken> {
    @Autowired
    private SysUserTokenMapper sysUserTokenMapper;

    @Autowired
    private RedisUtils redisUtils;

    public SysUserToken queryByToken(String token) {
        SysUserToken sysUserToken = redisUtils.get(Constant.TOKEN_NAME + token, SysUserToken.class);
        if (sysUserToken == null) {
            sysUserToken = sysUserTokenMapper.selectOne(new QueryWrapper<SysUserToken>().eq("token", token));

            if (sysUserToken != null) {
                String userId = Constant.TOKEN_NAME + sysUserToken.getUserId();
                redisUtils.set(userId, sysUserToken);

                String tokenStr = Constant.TOKEN_NAME + sysUserToken.getToken();
                redisUtils.set(tokenStr, sysUserToken);
            }
        }
        return sysUserToken;
    }

    public Map<String, Object> createToken(String userId) {
        //生成一个token
        String token = HexUtil.encodeHexStr(SecureUtil.md5(IdUtil.simpleUUID()), CharsetUtil.CHARSET_UTF_8);

//		String token =  jwtUtils.generateToken(userId);
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + Constant.EXPIRE * 1000);

        //判断是否生成过token
        SysUserToken tokenEntity = queryByUserId(userId);
        if(tokenEntity == null){
            tokenEntity = new SysUserToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            sysUserTokenMapper.insert(tokenEntity);
        }else{
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            sysUserTokenMapper.updateById(tokenEntity);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", userId);
        result.put("expire", Constant.EXPIRE);
        return result;
    }

    public SysUserToken queryByUserId(String userId) {
        SysUserToken sysUserToken = redisUtils.get(Constant.TOKEN_NAME + userId, SysUserToken.class);
        if(sysUserToken==null){
            sysUserToken=sysUserTokenMapper.selectById(userId);
            if(ObjectUtil.isNotNull(sysUserToken)){
                redisUtils.set(Constant.TOKEN_NAME + sysUserToken.getUserId(), sysUserToken);
                redisUtils.set(Constant.TOKEN_NAME + sysUserToken.getToken(), sysUserToken);
            }

        }
        return sysUserToken;
    }

    public void logout(String userId) {
        //生成一个token
        String token = HexUtil.encodeHexStr(SecureUtil.md5(IdUtil.simpleUUID()), CharsetUtil.CHARSET_UTF_8);
//		String token =  jwtUtils.generateToken(userId);
        //修改token
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        sysUserTokenMapper.update(tokenEntity,new UpdateWrapper<SysUserToken>().eq("user_id",userId));
    }
}
