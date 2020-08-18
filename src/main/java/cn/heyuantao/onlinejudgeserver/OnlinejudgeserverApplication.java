package cn.heyuantao.onlinejudgeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author he_yu
 * 确保不适用关系数据库
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class OnlinejudgeserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinejudgeserverApplication.class, args);
    }

}
