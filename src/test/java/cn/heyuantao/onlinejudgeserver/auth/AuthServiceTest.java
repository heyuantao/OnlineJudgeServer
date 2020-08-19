package cn.heyuantao.onlinejudgeserver.auth;

import cn.heyuantao.onlinejudgeserver.config.AuthKeyConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthKeyConfig authKeyConfig;

    @Test
    void displayAuthKeyConfigInformation() {
        System.out.println(authKeyConfig.getKeys());
        System.out.println(authKeyConfig.getSystemDefaultUsername());
        System.out.println(authKeyConfig.getSystemDefaultPassword());
    }
}