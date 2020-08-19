package cn.heyuantao.onlinejudgeserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 * 包含软件的密钥配置信息
 */
@ConfigurationProperties(prefix = "custom")
@Configuration
public class AuthKeyConfig {
    private List<String> keys = new ArrayList<String>(1);

    public List<String> getKeys(){
        return this.keys;
    }

    public void setKeys(List<String> keys){
        this.keys = keys;
    }
}
