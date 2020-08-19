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
     * 将Solution.id 对应的信息从待处理队列移动到判题完成队列
     * @param oneSolutionId
     */
    public Boolean moveSolutionToFinishedQueue(String oneSolutionId) {
        /**
         * 创建一个事务，保存所有命令一次执行完成
         */
        SessionCallback<Solution> callback = new SessionCallback() {
            /**
             * 将Solution对应的ID从PROCESSING队列移动到FINISHED队列，在插入到FINISHED队列的时候，将当前的时间信息随着ID一同存放。
             */
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                Double timeStampInDoubleFormat = getTimeStampInDoubleFormat();

                operations.multi();
                operations.opsForZSet().remove(processingQueueName,oneSolutionId);
                operations.opsForZSet().add(finishedQueueName, oneSolutionId, timeStampInDoubleFormat);
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
            String errorMessage = String.format("Error in move \'%s\' from processing queue to finished queue at moveSolutionToFinishedQueue()！",oneSolutionId);
            log.error(errorMessage);
            return Boolean.FALSE;
        }
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
        Set<String> expiredSolutionIdSet = redisTemplate.opsForZSet().rangeByScore(processingQueueName, begin, end);

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
            redisTemplate.opsForZSet().remove(finishedQueueName, oneSolutionId);

            /**
             * 再清除题目的相关信息
             */
            String key = solutionPrefix+oneSolutionId;
            if(redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
    }

    /**
     * 获取等待队列任务得数量
     * @return
     */
    public Long getPendingQueueLength() {
        Long count = redisTemplate.opsForList().size(processingQueueName);
        return count;
    }
}
