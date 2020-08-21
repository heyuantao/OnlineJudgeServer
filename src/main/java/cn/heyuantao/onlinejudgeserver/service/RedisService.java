package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.exception.InvalidValueException;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
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
     * 3、PROCESSING队列，存储正在处理的SOLUTION，该队列的类型为有序集合。该队列仅仅保存五分钟之内的信息。
     * 4、FINISHED队列，存储已经结束判题过程的SOLUTION，该队列的类型为有序集合。该队列仅仅保存五分种内的信息。
     */
    private String pendingQueueName       = "PENDING";
    private String processingQueueName    = "PROCESSING";
    private String finishedQueueName      = "FINISHED";
    private String solutionPrefix         = "SOLUTION::";

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
     * 更新已经存在在redis中的某一个solution
     * @param solution
     * @return
     */
    public Boolean updateSolutionAtRedis(Solution solution){
        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        SessionCallback<Solution> callback = new SessionCallback() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                /**
                 * 将Solution更新到Redis中
                 */
                operations.multi();
                String key = solutionPrefix+solution.getId();
                operations.opsForValue().set(key,solution);
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
            log.error("Error in updateSolutionAtRedis !");
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
        }catch (Exception ex){
            /**
             * 发生了未知的错误，很可能是Redis连接失败
             */
            String errorMessage = String.format("当执行getSolutionById()发生了未知的错误,请检查Redis的连接信息.未知错误信息为:%s",ex.getMessage());
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }

        if(solution==null){
            String errorMessage = String.format("编号为 \'%s\' 的Solution不存在",id);
            log.error(errorMessage);
            throw new InvalidValueException(errorMessage);
        }
        return solution;
    }


    /**
     * 从等待队列中移除一个任务，并将其加入待处理队列
     * 被加入的队列是排序集合，加入的时候同时将时间戳做完分数一同加入，确保能够进行正常排序,
     * @return 如果正常返回一个编号，否则返回null . 如果队列为空，返回值也为null
     */
    public String pickOneSolutionAndPutIntoProcessingQueue(){
        Double timeStampInDoubleFormat = getTimeStampInDoubleFormat();
        String solutionId = (String) redisTemplate.opsForList().leftPop(pendingQueueName);
        if(solutionId!=null){
            redisTemplate.opsForZSet().add(processingQueueName, solutionId, timeStampInDoubleFormat);
        }
        return solutionId;
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
     * 将Solution.id 对应的信息从待处理队列移动到判题完成队列
     * @param oneSolutionId
     */
    public Boolean moveSolutionToFinishedQueue(String oneSolutionId) {
        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        Double timeStampInDoubleFormat = getTimeStampInDoubleFormat();
        redisTemplate.opsForZSet().remove(processingQueueName,oneSolutionId);
        redisTemplate.opsForZSet().add(finishedQueueName, oneSolutionId, timeStampInDoubleFormat);
        return Boolean.TRUE;
    }

    /**
     * 检查一个ID是否在PROCESSING队列中
     * 由于在processingQueue中，每个id都有一个分数，如果某个id的分数为空则说明其不存在
     */
    public Boolean isInProcessingQueue(String id){
        Double score = redisTemplate.opsForZSet().score(processingQueueName,id);
        if(score == null){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }

    /**
     * 检查一个ID是否在FINISHED队列中
     * 由于在finishedQueue中，每个id都有一个分数，如果某个id的分数为空则说明其不存在
     */
    public Boolean isInFinishedQueue(String id){
        Double score = redisTemplate.opsForZSet().score(finishedQueueName,id);
        if(score == null){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }



    /**
     * 获取等待队列任务得数量
     * @return
     */
    public Long getPendingQueueLength() {
        Long count = redisTemplate.opsForList().size(pendingQueueName);
        return count;
    }

    /**
     * Get and Set 方法
     * @return
     */
    public String getPendingQueueName() {
        return pendingQueueName;
    }

    public String getProcessingQueueName() {
        return processingQueueName;
    }

    public String getFinishedQueueName() {
        return finishedQueueName;
    }

    public String getSolutionPrefix() {
        return solutionPrefix;
    }
}
