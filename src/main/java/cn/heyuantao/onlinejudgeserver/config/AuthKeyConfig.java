package cn.heyuantao.onlinejudgeserver.config;

import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author he_yu
 * 包含软件的密钥配置信息
 */
@ConfigurationProperties(prefix = "custom")
@Configuration
public class AuthKeyConfig {
    /**
     * 系统使用Token进行认证，但是Spring Security要求认证完成后需要返回UserDetails的结构体的实例
     * 因此当认证成功后将systemDefaultUsername和systemDefaultPassword作为一个默认的系统用户
     */

    private String systemDefaultUsername = "admin";
    private String systemDefaultPassword = UUID.randomUUID().toString().replace("-", "").toLowerCase();

    /**
     * 认证用的Token列表，其内容来源于application.yaml文件中的custom.keys下的内容
     */
    private List<String> keys = new ArrayList<String>(1);

    public List<String> getKeys(){
        return this.keys;
    }

    public void setKeys(List<String> keys){
        this.keys = keys;
    }

    public String getSystemDefaultUsername() {
        return systemDefaultUsername;
    }

    public String getSystemDefaultPassword() {
        return systemDefaultPassword;
    }
}
