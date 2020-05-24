package com.rs.hr.modules.sys.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rs.hr.common.Constant;
import com.rs.hr.common.Result;
import com.rs.hr.common.google.GoogleAuthUtils;
import com.rs.hr.config.oauth2.OAuth2Token;
import com.rs.hr.modules.sys.entity.SysGoogleAuthKey;
import com.rs.hr.modules.sys.entity.SysUser;
import com.rs.hr.modules.sys.service.SysGoogleAuthKeyService;
import com.rs.hr.modules.sys.service.SysUserService;
import com.rs.hr.modules.sys.service.SysUserTokenService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author sxia
 * @Description: TODO(登录相关)
 * @date 2017-6-23 15:07
 */
@RestController
public class SysLoginController extends AbstractController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysGoogleAuthKeyService sysGoogleAuthKeyService;
	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Result login(String username, String password)throws IOException {

		//用户信息
		SysUser user = sysUserService.queryByUserName(username);

		//账号不存在
		if(user == null) {
			return Result.error("账号不存在");
		}

		//密码错误
		if(!user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
			return Result.error("密码不正确");
		}

		//账号锁定
		if(Constant.UserStatus.DISABLE.getValue() == user.getStatus()){
			return Result.error("账号已被锁定,请联系管理员");
		}

		//根据用户获取谷歌令牌二维码图片
		Map<String, Object> result = sysGoogleAuthKeyService.getIMG(username);
		result.put("userid", user.getId());
		Result r =Result.ok().put(result);
		return r;
	}

	/**
	 * 谷歌令牌验证
	 */
	@RequestMapping(value = "/sys/googleAuthKey", method = RequestMethod.POST)
	public Result googleAuthKey(String username, String authKey, String userid){

		//校验输入格式
		try{
			Long.valueOf(authKey);
		}catch(Exception ex){
			return Result.error("令牌格式不正确, 正确格式：数字");
		}

		//获取用户的谷歌令牌密钥
		SysGoogleAuthKey googleAuthKey = sysGoogleAuthKeyService.queryByUsername(username);


		//判断用户输入的令牌数字与谷歌绑定的是否匹配
		if (GoogleAuthUtils.verify(googleAuthKey.getSecret(), Long.valueOf(authKey))) {

			//匹配成功更新DB,设置令牌激活状态为 1 (已激活令牌)
			googleAuthKey.setStatusFlag(true);
			sysGoogleAuthKeyService.update(googleAuthKey,new UpdateWrapper<>());

			//生成token，并保存到数据库
			Map<String, Object> result = sysUserTokenService.createToken(userid);

//			Subject currentUser = SecurityUtils.getSubject();
//			OAuth2Token token = new OAuth2Token(result.get("token").toString());
//			currentUser.login(token);
			Result r =Result.ok().put(result);
			return r;
		}else{
			return Result.error("令牌不正确");
		}



	}


	/**
	 * 退出
	 */
	@RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
	public Result logout() {
		sysUserTokenService.logout(getUserId());
		return Result.ok();
	}

}
