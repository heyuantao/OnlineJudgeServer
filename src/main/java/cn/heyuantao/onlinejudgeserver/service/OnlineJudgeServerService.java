package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author he_yu
 * 面向第三方的接口，用户更新题目
 */
@Slf4j
@Service
public class OnlineJudgeServerService {

    @Autowired
    RedisService redisService;

    /**
     * 统计当前待处理得问题得数量
     * @return
     */
    public Long getSolutionCount(){
        return redisService.getPendingQueueLength();
    }

    /**
     * 根据输入的问题，保存到Redis中，并将对应的ID返回
     * @param problem
     * @return
     */
    public Solution createSolutionByProblem(Problem problem){
        Solution solution = new Solution();
        String idStr = UUIDGenerator.generateSolutionKey();
        solution.setId(idStr);

        solution.setProblem(problem);

        Result result = new Result();
        solution.setResult(result);

        /**
         * 从Problem创建一个新的Solution,并将其加入到Redis中
         */
        redisService.insertSolutionIntoRedis(solution);

        return solution;
    }

    /**
     * 这个是一个耗时的任务，请在异步任务中进行调用
     * 返回True表示同时成功,False表示通知失败
     */
    public Boolean notifyClientBySolution(Solution solution) {
        String notifyUrl = solution.getProblem().getNotifyAddress();
        Result judgeResult = solution.getResult();
        //错误重试的次数
        Integer retryCount = 1;
        Integer maxRetryCount = 3;


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        //以JSON的方式发送数据
        headers.setContentType(MediaType.APPLICATION_JSON);
        //以JSON的方式接受数据
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Result> entity = new HttpEntity<>(judgeResult,headers);

        //进行三次重试
        for(retryCount =1; retryCount<=maxRetryCount; ){
            //通知第三方客户端，这个过程可能耗时较长，且可能会出错
            System.out.println("Notify "+notifyUrl);
            System.out.println("With "+entity);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(notifyUrl,entity,String.class);

            //如下是几个被认为通知成功的状态
            Set<HttpStatus> acceptHttpStatusSet = new HashSet<HttpStatus>();
            acceptHttpStatusSet.add(HttpStatus.OK);
            acceptHttpStatusSet.add(HttpStatus.ACCEPTED);
            acceptHttpStatusSet.add(HttpStatus.CREATED);

            if (acceptHttpStatusSet.contains(responseEntity.getStatusCode())) {
                log.info("Notify client work !");
                return Boolean.TRUE;
            } else {
                retryCount = retryCount +1;
                String errorMessage = String.format("Notify the third part client failure. Retry it to post url \'%s\' !",notifyUrl);
                log.error(errorMessage);
            }
        }

        //如果执行到此处，说明重试几次都失败了，记录下日志。
        String errorMessage = String.format("Notify the third part with %d times , Please check the  post url \'%s\' !",maxRetryCount, notifyUrl);
        log.error(errorMessage);

        return Boolean.FALSE;
    }

    /**
     * 获取一个任务的判题状态,该任务可能不存在
     * @param id
     * @return
     */
    public Result getProblemJudgeResultById(String id) {
        Solution solution = redisService.getSolutionById(id);
        Result result = solution.getResult();
        return result;
    }

    /**
     * 返回一个任务对应的状态,状态为一下几种
     * PENDING(等待)、PROCESSING(处理中)、FINISHED(结束)
     * @param id
     * @return
     */
    public String getProblemJudgeSummaryById(String id){
        Solution solution = redisService.getSolutionById(id);
        Result result = solution.getResult();
        JudgeStatus judgeStatus = result.getJudgeStatus();
        String finalStatusDescribe = "";

        if(judgeStatus.isInPendingStatus()){
            finalStatusDescribe="PENDING";
        }else if(redisService.isInProcessingQueue(id)){
            finalStatusDescribe="PROCESSING";
        }else if(redisService.isInFinishedQueue(id)){
            finalStatusDescribe="FINISHED";
        }
        return finalStatusDescribe;
    }
}
