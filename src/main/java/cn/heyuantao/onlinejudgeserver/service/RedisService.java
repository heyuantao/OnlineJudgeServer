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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

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
     * 获取一个Solution,如果出错则返回null
     * @param id
     * @return
     */
    public Solution getSolutionById(String id){
        String key = solutionPrefix+id;
        Solution solution = null;
        try{
            solution = (Solution) redisTemplate.opsForValue().get(key);
            return solution;
        }catch (Exception ex){
            String errorMessage = String.format("getSolutionById error on %s",id);
            log.error(errorMessage);
            return null;
        }
    }

    /**
     * 从等待队列中移除一个任务，并将其加入待处理队列
     * 被加入的队列是排序集合，加入的时候同时将时间戳做完分数一同加入，确保能够进行正常排序
     * @return 如果正常返回一个编号，否则返回null
     */
    public String pickOneSolutionAndPutIntoProcessingQueue(){
        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        SessionCallback<Solution> callback = new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                /**
                 * 时间戳，这个值被一块加入用于排序
                 */
                Double timeStampInDoubleFormat = getTimeStampInDoubleFormat();

                operations.multi();
                String solutionId = (String) operations.opsForList().leftPop(pendingQueueName);
                operations.opsForZSet().add(processingQueueName, solutionId, timeStampInDoubleFormat);
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
     * 以秒的方式返回当前的时间，并将其返回值转变为浮点的格式
     * 尽管Long到Double转换存在损失精度的可能
     * 但只要确保Long的范围在 -2^52 and 2^52 之间,这个转换不会出现问题
     * @return 返回浮点值
     */
    public Double getTimeStampInDoubleFormat(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Long toEpochSecond = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        return toEpochSecond.doubleValue();
    }

    /**
     * 检查Processing队列的任务，看看是否有些任务属于太长时间没有完成的，这些任务可能是判题机出错导致的情况
     * 每隔二十秒执行一次,并清除超时5分钟以前的Solution
     * 一个题目在提交后会保存为一个Solution，如果超过五分钟该题目仍然没有被解决可能对应的判题机出错
     */
    @Scheduled(fixedRate = 20000)
    public void clearExpiredSolution(){
        log.info("Clear the expire solution !");
        LocalDateTime localDateTime = LocalDateTime.now().minus(5, ChronoUnit.MINUTES);
        Long endDateTimeInLongFormat = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        Double begin = new Double(0);
        Double end = endDateTimeInLongFormat.doubleValue();
        /**
         * 获取五分钟以前的所有任务编号
         */
        Set<String> expiredSolutionIdSet = redisTemplate.opsForZSet().rangeByScore(processingQueueName, begin, end);

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
            redisTemplate.opsForZSet().remove(processingQueueName, oneSolutionId);

            /**
             * 再清除题目的相关信息
             */
            String key = solutionPrefix+oneSolutionId;
            if(redisTemplate.hasKey(key)){
                redisTemplate.delete(key);
            }else{
                log.error("The solution "+key+" should exist !");
            }
        }
    }
}
