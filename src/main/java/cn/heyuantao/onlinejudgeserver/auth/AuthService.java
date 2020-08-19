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

    /**
     * AuthKeyConfig是一个配置信息类，其中含有在Authorization Header认证用的Token,同时该类也存放了UserDetails中的默认用户名和密码
     */
    @Autowired
    AuthKeyConfig authKeyConfig;


    /**
     * 根据Token来返回用户的实例
     * @param userToken
     * @return
     */
    public SysUser loadSysUserByToken(String userToken){
        List<String> keyList = authKeyConfig.getKeys();
        if(keyList.contains(userToken)){
            return new SysUser(authKeyConfig.getSystemDefaultUsername(),authKeyConfig.getSystemDefaultPassword());
        }else{
            return null;
        }
    }
}
