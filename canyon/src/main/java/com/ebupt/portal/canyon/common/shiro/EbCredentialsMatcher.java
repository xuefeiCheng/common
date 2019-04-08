package com.ebupt.portal.canyon.common.shiro;

import com.ebupt.portal.canyon.common.util.EncryptUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * 自定义Shiro密码比较器
 *
 * @author chy
 * @date 2019-03-11 10:31
 */
public class EbCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// 获取用户密码和盐值
		SimpleAuthenticationInfo asInfo = (SimpleAuthenticationInfo) info;
		String pwd = (String) asInfo.getCredentials();
		asInfo.getCredentialsSalt().toHex();
		String salt = new String(Base64.decodeBase64(asInfo.getCredentialsSalt().toBase64()));

		// 获取用户输入的密码
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String inPwd = EncryptUtil.md5WithSpecialSalt(new String(upToken.getPassword()), salt);

		return this.equals(inPwd, pwd);
	}
}
