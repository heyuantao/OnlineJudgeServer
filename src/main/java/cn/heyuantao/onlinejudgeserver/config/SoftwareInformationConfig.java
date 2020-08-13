package cn.heyuantao.onlinejudgeserver.config;

import cn.heyuantao.onlinejudgeserver.domain.SoftwareInformation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author he_yu
 * 软件的版本和运行状态信息
 */
@Configuration
public class SoftwareInformationConfig {
    @Value("${profile.name:dev}")
    private String runStatus;

    @Bean
    public SoftwareInformation getSoftware(){
        SoftwareInformation softwareInformation=new SoftwareInformation();
        softwareInformation.setVersion("0.1");
        softwareInformation.setTitle("判题系统");
        softwareInformation.setDescription("编程判题接口，用于完成判题机和第三方客户端的连接");

        /**
         * 设置在软件基本信息界面显示的内容
         */
        if(StringUtils.equalsIgnoreCase(runStatus, "dev")){
            softwareInformation.setRunStatus("调试模式");
        }else if(StringUtils.equalsIgnoreCase(runStatus, "prod")){
            softwareInformation.setRunStatus("发布模式");
        }else{
            softwareInformation.setRunStatus("异常模式");
        }

        return softwareInformation;
    }
}
