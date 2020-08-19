package cn.heyuantao.onlinejudgeserver.auth;

import cn.heyuantao.onlinejudgeserver.config.AuthKeyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 */

@Service
public class AuthService {

    @Autowired
    AuthKeyConfig authKeyConfig;
    /**
     * 根据Token来返回用户的实例
     * @param userToken
     * @return
     */
    public SysUser loadSysUserByToken(String userToken){
        if(userToken.equals("abc123")){
            return new SysUser("user","nopassword");
        }else{
            return null;
        }
    }
}
