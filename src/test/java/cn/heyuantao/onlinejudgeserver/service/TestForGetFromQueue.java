package cn.heyuantao.onlinejudgeserver.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestForGetFromQueue {

    @Autowired
    RedisTemplate redisTemplate;

    private String pendingQueueName       = "PENDING";

    @BeforeEach
    public void prepare(){
        System.out.println("Insert Data");

    }

    @Test
    public void getFromQueue(){
        redisTemplate.opsForList().rightPush(pendingQueueName,"xxxxxxxxx");
        for(int i=0;i<10;i++){
            String content = (String) redisTemplate.opsForList().leftPop(pendingQueueName);
            System.out.println(content);
        }

    }
}
