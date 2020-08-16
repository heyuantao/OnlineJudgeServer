package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.Result;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

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
    void timeStampTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long timeStamp = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(timeStamp);
    }
}