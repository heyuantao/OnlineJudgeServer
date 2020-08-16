package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Solution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author he_yu
 * 负责将相应的数据保存在Redis中
 */
@Slf4j
@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 在Redis要存储三种类型的信息
     * 1、Solution本身，用SOLUTION作为前缀
     * 2、PENDING队列，存储待待处理的SOLUTION, 该队列的类型为LIST
     * 3、PROCESSING队列，存储正在处理的SOLUTION，该队列的类型为有序集合
     */
    private String pendingQueueName       = "PENDING";
    private String processingQueueName    = "PROCESSING";
    private String solutionPrefix           = "SOLUTION::";

    /**
     * 将Solution保存在Redis中，同时将其加入等待队列,
     * @param solution
     * @return 如果保存成功，则返回True，负责返回False
     */
    public Boolean insertSolutionIntoRedis(Solution solution){

        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        SessionCallback<Solution> callback = new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                /**
                 * 将问题保存到Redis中，同时将题目编号保存到等待队列中
                 */
                operations.multi();
                String key = solutionPrefix+solution.getId();
                operations.opsForValue().set(key,solution);
                operations.opsForList().rightPush(pendingQueueName,solution.getId());
                return operations.exec();
            }
        };


        try{
            /**
             * 返回值为ArrayList<Object>,其中每个Object代表了命令的执行情况
             */
            List<Object> objectList = (List<Object>) redisTemplate.execute(callback);
            return Boolean.TRUE;
        }catch (Exception ex){
            log.error("Error in insertSolutionIntoRedis !");
            return Boolean.FALSE;
        }
    }


    /**
     * 从等待队列中移除一个任务，并将其加入待处理队列
     * @return 如果正常返回一个编号，否则返回null
     */
    public String pickOneSolutionAndPutIntoProcessingQueue(){
        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        SessionCallback<Solution> callback = new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                LocalDateTime localDateTime = LocalDateTime.now();
                Long timeStamp = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
                operations.multi();
                String solutionId = (String) operations.opsForList().leftPop(pendingQueueName);
                //operations.opsForSet().add(processingQueueName,solutionId);
                operations.opsForZSet().add(processingQueueName,solutionId,timeStamp);
                operations.exec();
                return solutionId;
            }
        };


        try{
            String solutionId = (String) redisTemplate.execute(callback);
            return solutionId;
        }catch (Exception ex){
            log.error("Error in pickOneSolutionAndPutIntoProcessingQueue !");
            return null;
        }
    }

    /**
     * 检查Processing队列的任务，看看是否有些任务属于太长时间没有完成的，这些任务可能是判题机出错
     * 导致的情况
     * 每隔六秒执行一次
     */
    @Scheduled(fixedRate = 6000)
    public void clearUnfinishedSolution(){
        System.out.println("Task run for clean solution!");
    }
}
