package cn.heyuantao.onlinejudgeserver.auth;

import org.springframework.stereotype.Service;

/**
 * @author he_yu
 */
@Service
public class AuthService {

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
