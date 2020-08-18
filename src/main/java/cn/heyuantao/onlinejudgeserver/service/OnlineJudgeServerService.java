package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.Result;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
     * @param solution
     */
    public Boolean notifyClientBySolution(Solution solution) {
        String notifyUrl = solution.getProblem().getNotifyAddress();
        Integer retryCount =0;
        RestTemplate restTemplate = new RestTemplate();

        /**
         * 通知第三方客户端，这个过程可能耗时较长，且可能会出错
         */
        ResponseEntity<Result> responseEntity = restTemplate.postForEntity(notifyUrl,solution.getResult(),Result.class);

        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            log.info("Notify client work !");
        } else {
            String errorMessage = String.format("向 %s 发送请求时失败!",notifyUrl);
            log.error("Notify client work !");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
