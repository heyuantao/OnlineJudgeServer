package cn.heyuantao.onlinejudgeserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * @author he_yu
 */
@Slf4j
@Service
public class RedisCron {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisService redisService;

    /**
     * 检查Processing队列的任务，看看是否有些任务属于太长时间没有完成的，这些任务可能是判题机出错导致的情况
     * 每隔二十秒执行一次,并清除超时5分钟以前的Solution
     * 一个题目在提交后会保存为一个Solution，如果超过五分钟该题目仍然没有被解决可能对应的判题机出错
     */
    @Scheduled(fixedRate = 20000)
    public void clearExpiredSolutionInProcessingQueue(){
        log.info("Clear the expire solution in the processing queue !");
        LocalDateTime localDateTime = LocalDateTime.now().minus(5, ChronoUnit.MINUTES);
        Long endDateTimeInLongFormat = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        Double begin = new Double(0);
        Double end = endDateTimeInLongFormat.doubleValue();
        /**
         * 获取五分钟以前的所有任务编号
         */
        Set<String> expiredSolutionIdSet = redisTemplate.opsForZSet().rangeByScore(redisService.getProcessingQueueName(), begin, end);

        if(expiredSolutionIdSet.size()>0){
            log.error("Some solution "+expiredSolutionIdSet+" expire for some reson !");
        }else{
            log.info("No expire solution found !");
        }

        /**
         * 准备清除对应的题目信息
         */
        for(String oneSolutionId:expiredSolutionIdSet){
            /**
             * 先清除待处理队列中的内容
             */
            redisTemplate.opsForZSet().remove(redisService.getProcessingQueueName(), oneSolutionId);

            /**
             * 再清除题目的相关信息
             */
            String key = redisService.getSolutionPrefix() + oneSolutionId;
            if(redisTemplate.hasKey(key)){
                redisTemplate.delete(key);
            }else{
                log.error("The solution "+key+" should exist !");
            }
        }
    }

    /**
     * 检查FINISHED队列的任务，清除10分钟以前进入队列的任务，该队列的任务是已经判题结束的任务，这些Solution会在系统中留存一段时间用于客户端的主动查询
     * 每隔三十秒执行一次,并清除超时10分钟以前的Solution
     */
    @Scheduled(fixedRate = 30*1000)
    public void clearCachedSolutionIdInFinishedQueue(){
        log.info("Clear the cached solution in the finished queue !");
        LocalDateTime localDateTime = LocalDateTime.now().minus(10, ChronoUnit.MINUTES);
        Long endDateTimeInLongFormat = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        Double begin = new Double(0);
        Double end = endDateTimeInLongFormat.doubleValue();
        /**
         * 获取10分钟以前的所有任务编号
         */
        Set<String> expiredSolutionIdSet = redisTemplate.opsForZSet().rangeByScore(redisService.getProcessingQueueName(), begin, end);

        if(expiredSolutionIdSet.size()>0){
            log.info("Clear "+expiredSolutionIdSet+" cached solution before sometime !");
        }else{
            log.info("No cached solution should be clear !");
        }

        /**
         * 准备清除对应的题目信息
         */
        for(String oneSolutionId:expiredSolutionIdSet){
            /**
             * 先清除待完成队列的内容
             */
            redisTemplate.opsForZSet().remove(redisService.getFinishedQueueName(), oneSolutionId);

            /**
             * 再清除题目的相关信息
             */
            String key = redisService.getSolutionPrefix() + oneSolutionId;
            if(redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
    }
}
