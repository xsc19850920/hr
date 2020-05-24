package com.rs.hr.modules.sys.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.db.sql.SqlFormatter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.hr.common.Constant;
import com.rs.hr.common.SQLFilter;
import com.rs.hr.common.google.GoogleAuthUtils;
import com.rs.hr.modules.sys.entity.SysGoogleAuthKey;
import com.rs.hr.modules.sys.mapper.SysGoogleAuthKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class SysGoogleAuthKeyService extends ServiceImpl<SysGoogleAuthKeyMapper, SysGoogleAuthKey> implements IService<SysGoogleAuthKey> {
    @Autowired
    private SysGoogleAuthKeyMapper sysGoogleAuthKeyMapper;

    /**
     * 判断用户是否需要生成谷歌令牌二维码
     * @param username
     * @return
     */

    public Map<String, Object> getIMG(String username) {
        username = SQLFilter.sqlInject(username);
        Map<String, Object> result = new HashMap<>();

        //根据用户获取谷歌令牌信息
        SysGoogleAuthKey googleAuthKey = sysGoogleAuthKeyMapper.selectOne(new QueryWrapper<SysGoogleAuthKey>().eq("username",username).eq("del_flag",Constant.DEL_FLAG_0));

        //用户未绑定过令牌或者扫描二维码后未登陆过，重新生成谷歌令牌二维码
        if(googleAuthKey == null || !googleAuthKey.getStatusFlag() ){
            String secret = GoogleAuthUtils.genSecret();
            String qrCode = GoogleAuthUtils.getQRCode(username, secret);
            String img = GoogleAuthUtils.getImageByZxing(qrCode, 200);
            result.put("img", img);

            if(googleAuthKey == null){
                //用户未生成过谷歌令牌二维码
                googleAuthKey = new SysGoogleAuthKey();
                googleAuthKey.setStatusFlag(false);
                googleAuthKey.setUsername(username);
                googleAuthKey.setSecret(secret);
                googleAuthKey.setResetFlag(false);
                googleAuthKey.setDelFlag(false);
                googleAuthKey.setAuthKeyId(IdUtil.simpleUUID());
                googleAuthKey.setCreateTime(new Date());
                googleAuthKey.setModifyTime(new Date());
                //DB插入
                save(googleAuthKey);
            }else{
                //用户生成过二维码，但是当时激活令牌
                googleAuthKey.setSecret(secret);
                googleAuthKey.setModifyTime(new Date());
                sysGoogleAuthKeyMapper.update(googleAuthKey,new UpdateWrapper<>());
            }
        }

        //用户提交了重置令牌的申请
        if(googleAuthKey != null && googleAuthKey.getResetFlag() ){
            result.put("warning", "令牌重置申诉中");
        }
        return result;
    }

    public SysGoogleAuthKey queryByUsername(String username) {
        username = SQLFilter.sqlInject(username);
        return sysGoogleAuthKeyMapper.selectOne(new QueryWrapper<SysGoogleAuthKey>().eq("username",username).eq("del_flag",Constant.DEL_FLAG_0));
    }
}
