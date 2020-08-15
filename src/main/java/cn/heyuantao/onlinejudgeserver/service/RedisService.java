package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Solution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

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
     * 2、PENDING队列，存储待待处理的SOLUTION
     * 3、PROCESSING队列，存储正在处理的SOLUTION
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
                operations.multi();
                operations.opsForValue().set(solutionPrefix+solution.getId(),solution);
                operations.opsForList().rightPush(pendingQueueName,solution.getId());
                //operations.opsForValue().set("ok","good");
                //operations.opsForValue().increment("ok");
                return operations.exec();
            }
        };


        try{
            /**
             * 返回值为ArrayList<Object>,其中每个Object代表了命令的执行情况
             */
            List<Object> objectList = (List<Object>) redisTemplate.execute(callback);
        }catch (Exception ex){
            log.error("Error in insertSolutionIntoRedis !");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;

        /**
         * 根据返回值判断命令的执行情况，可能不需要这个操作，即如果出现错误，可能之间就抛出异常
         */
/*        if(objectList==null){
            return Boolean.TRUE;
        }else{
            return Boolean.TRUE;
        }*/
    }
}
