package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.Result;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate redisTemplate;


    private String pendingQueueName       = "PENDING";
    private String processingQueueName    = "PROCESSING";
    private String solutionPrefix         = "SOLUTION::";

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

    @Test
    void getTimeStampInDoubleFormat() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Long toEpochSecond = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        String str = Long.toBinaryString(toEpochSecond);
        System.out.println(str);
        System.out.println(str.length());
    }

    @Test
    void clearExpiredSolution() throws InterruptedException {

        redisTemplate.opsForZSet().add(processingQueueName,UUIDGenerator.generateSolutionKey(),redisService.getTimeStampInDoubleFormat());
        redisTemplate.opsForZSet().add(processingQueueName,UUIDGenerator.generateSolutionKey(),redisService.getTimeStampInDoubleFormat());
        redisTemplate.opsForZSet().add(processingQueueName,UUIDGenerator.generateSolutionKey(),redisService.getTimeStampInDoubleFormat());

        //Thread.sleep(1000*2);

        LocalDateTime localDateTime = LocalDateTime.now().minus(15, ChronoUnit.SECONDS);
        Long endDateTimeInLongFormat = localDateTime.toEpochSecond(ZoneOffset.of("+8"));

        Double begin = new Double(0);
        Double end = endDateTimeInLongFormat.doubleValue();

        Set<String> expiredSolutionIdSet = redisTemplate.opsForZSet().rangeByScore(processingQueueName, begin, end);

        if(expiredSolutionIdSet.size()>0){
            System.out.println("Some solution "+expiredSolutionIdSet+" expire for some reson !");
        }else{
            System.out.println("No expire solution");
        }
    }
}