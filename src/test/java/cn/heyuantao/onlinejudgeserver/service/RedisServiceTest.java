package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.Result;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void insertSolutionIntoRedis() {
        Solution solution = new Solution();
        solution.setId(UUIDGenerator.generateSolutionKey());
        solution.setProblem(new Problem());
        solution.setResult(new Result());
        System.out.println(solution);

        Boolean status = redisService.insertSolutionIntoRedis(solution);
        System.out.println(status);
    }

    @Test
    void timeStampDisplayTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long timeStamp = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(timeStamp);
    }

    @Test
    void timeStampRangeFormat() throws InterruptedException {
        Double value = null;
        Double old = null;
        value =redisService.getTimeStampInDoubleFormat();
        redisTemplate.opsForZSet().add("test",1,value);
        value =redisService.getTimeStampInDoubleFormat();
        redisTemplate.opsForZSet().add("test",2,value);
        value =redisService.getTimeStampInDoubleFormat();
        redisTemplate.opsForZSet().add("test",3,value);

        Thread.sleep(1000);

        value = redisService.getTimeStampInDoubleFormat();
        old = value;
        redisTemplate.opsForZSet().add("test",4,value);
        Set<Double> doubleSet = redisTemplate.opsForZSet().rangeByScore("test",old ,value);

        System.out.println(doubleSet);

    }
}