package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.core.LanguageType;
import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemResourceLimit;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import cn.heyuantao.onlinejudgeserver.exception.BindingResultException;
import cn.heyuantao.onlinejudgeserver.service.OnlineJudgeServerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<Solution> createProblem(
            @Validated @RequestBody ProblemRequestDTO problemRequestDTO,
            BindingResult bindingResult
    ) throws Exception {
        if(bindingResult.hasErrors()){
            throw new BindingResultException(bindingResult);
        }

        Problem problem = convertToProblem(problemRequestDTO);
        Solution solution = onlineJudgeServerService.createSolutionByProblem(problem);

        return new ResponseEntity(solution, HttpStatus.ACCEPTED);
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
        problemResourceLimit.setSpecialJudge(problemRequestDTO.getIsSpecialJudge());
        problem.setProblemResourceLimit(problemResourceLimit);

        problem.setTestCaseList(problemRequestDTO.getTestCaseList());

        return problem;
    }
}
