package com.ebupt.portal.shiro.configs;

import com.ebupt.portal.common.utils.EncryptionUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class EBCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 获取用户输入的密码
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String inPwd = EncryptionUtil.md5(new String(uToken.getPassword()));

        // 正确的密码
        String pwd = (String) info.getCredentials();
        return this.equals(inPwd, pwd);
    }
}
