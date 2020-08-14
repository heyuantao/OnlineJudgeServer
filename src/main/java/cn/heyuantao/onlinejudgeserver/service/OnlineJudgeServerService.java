package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.UUIDGenerator;
import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.Result;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import org.springframework.stereotype.Service;

/**
 * @author he_yu
 * 面向第三方的接口，用户更新题目
 */
@Service
public class OnlineJudgeServerService {

    /**
     * 根据输入的问题，保存到Redis中，并将对应的ID返回
     * @param problem
     * @return
     */
    public String createSolution(Problem problem){
        Solution solution = new Solution();
        String idStr = UUIDGenerator.generateSolutionKey();
        solution.setId(idStr);
        solution.setProblem(problem);

        Result result = new Result();
        solution.setResult(result);

        return idStr;
    }
}
