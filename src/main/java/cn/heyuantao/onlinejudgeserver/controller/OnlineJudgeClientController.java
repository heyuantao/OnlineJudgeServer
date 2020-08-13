package cn.heyuantao.onlinejudgeserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author he_yu
 * 处理来自判题机的各种请求
 */
@Controller
@RequestMapping("/api/onlinejudgeclient/")
public class OnlineJudgeClientController {

    @GetMapping
    public ResponseEntity<String> handleClientRequest(){
        return new ResponseEntity("hello", HttpStatus.ACCEPTED);
    }
}
