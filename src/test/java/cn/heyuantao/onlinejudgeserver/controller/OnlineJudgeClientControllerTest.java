package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.auth.SysUser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OnlineJudgeClientControllerTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

}