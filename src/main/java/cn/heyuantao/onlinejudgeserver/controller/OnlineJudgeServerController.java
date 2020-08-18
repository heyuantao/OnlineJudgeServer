package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.core.*;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import cn.heyuantao.onlinejudgeserver.exception.BindingResultException;
import cn.heyuantao.onlinejudgeserver.service.OnlineJudgeServerService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author he_yu
 * 客户端接口，为
 */
@Slf4j
@Api(tags={"客户端接口"})
@Controller
@RequestMapping("/api/v1/onlinejudgeserver/")
public class OnlineJudgeServerController {

    @Autowired
    OnlineJudgeServerService onlineJudgeServerService;

    @PostMapping("/problem/")
    public ResponseEntity<Map<String,Object>> createProblem(
            @Validated @RequestBody ProblemRequestDTO problemRequestDTO,
            BindingResult bindingResult
    ) throws Exception {
        if(bindingResult.hasErrors()){
            throw new BindingResultException(bindingResult);
        }

        Problem problem = convertToProblem(problemRequestDTO);

        Solution solution = onlineJudgeServerService.createSolutionByProblem(problem);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",solution.getId());
        map.put("notifyUrl",solution.getProblem().getNotifyAddress());

        return new ResponseEntity(map, HttpStatus.ACCEPTED);
    }

    /**
     * 查询某个ID对应的状态
     * @param id
     * @return
     */
    @GetMapping("/problem/status/{id}/")
    public ResponseEntity<Map<String,Object>> problemStatus(
            @PathVariable String id
    ){
        Result judgeResult = onlineJudgeServerService.getProblemJudgeResultById(id);
        String judgeStatusSummary = onlineJudgeServerService.getProblemJudgeSummaryById(id);

        Map<String,Object> map = new HashMap<String,Object>(7);
        map.put("id",id);
        map.put("judgeStatus",judgeResult.getJudgeStatus().getName());
        map.put("time",judgeResult.getTime());
        map.put("memory",judgeResult.getMemory());
        map.put("passRate",judgeResult.getPassRate());
        map.put("compileErrorInformation",judgeResult.getCompileErrorInformation());
        map.put("runErrorInformation",judgeResult.getRunErrorInformation());
        //该字段为题目所处的状态描述，为所述状态中的一个。PENDING(等待)、PROCESSING(处理中)、FINISHED(结束)
        map.put("summary",judgeStatusSummary);

        return new ResponseEntity(map, HttpStatus.ACCEPTED);
    }


    public Problem convertToProblem(ProblemRequestDTO problemRequestDTO)  {
        Problem problem = new Problem();

        problem.setSourceCode(problemRequestDTO.getSourceCode());
        problem.setNotifyAddress(problemRequestDTO.getNotifyAddress());

        LanguageType languageType = LanguageType.getLanguageTypeByExtension(problemRequestDTO.getLangExtension());
        problem.setLanguageType(languageType);

        ProblemResourceLimit problemResourceLimit = new ProblemResourceLimit();
        problemResourceLimit.setMemoryLimit(problemRequestDTO.getMemoryLimit());
        problemResourceLimit.setTimeLimit(problemRequestDTO.getTimeLimit());
        problemResourceLimit.setIsSpecialJudge(problemRequestDTO.getIsSpecialJudge());
        problem.setProblemResourceLimit(problemResourceLimit);

        problem.setTestCaseList(problemRequestDTO.getTestCaseList());

        return problem;
    }
}
