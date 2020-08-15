package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.domain.ProblemRequestDTO;
import cn.heyuantao.onlinejudgeserver.exception.BindingResultException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Api(tags={"客户端接口"})
@Controller
@RequestMapping("/api/v1/onlinejudgeserver/")
public class OnlineJudgeServerController {

    @PostMapping
    public ResponseEntity<String> createSolution(
            @Validated @RequestBody ProblemRequestDTO problemRequestDTO,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            throw new BindingResultException(bindingResult);
        }

        System.out.println(problemRequestDTO);
        //log.debug(problemRequestDTO.toString());

        return new ResponseEntity("work", HttpStatus.ACCEPTED);
    }
}
