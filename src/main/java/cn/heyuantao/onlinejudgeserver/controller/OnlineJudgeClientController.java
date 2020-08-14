package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.service.OnlineJudgeClientService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author he_yu
 * 处理来自判题机的各种请求
 */
@Api(tags={"判题机接口"})
@Controller
@RequestMapping("/api/v1/onlinejudgeclient/")
public class OnlineJudgeClientController {

    @Autowired
    OnlineJudgeClientService onlineJudgeClientService;

    @PostMapping("/getjobs/")
    public ResponseEntity<String> getJobs(){
        List<String> jobs = onlineJudgeClientService.getJobs();
        StringBuilder stringBuilder = new StringBuilder();
        for(String oneLine:jobs){
            stringBuilder.append(oneLine);
            stringBuilder.append("\n");
        }
        return new ResponseEntity(stringBuilder.toString(),HttpStatus.ACCEPTED);
    }


    @PostMapping("/updatesolution/")
    public ResponseEntity<String> updateSolution(
            @RequestParam(value="sid") String sid,
            @RequestParam(value="result") String result,
            @RequestParam(value="time") String time,
            @RequestParam(value="memory") String memory,
            @RequestParam(value="sim") String sim,
            @RequestParam(value="sim_id") String sim_id,
            @RequestParam(value="pass_rate") String pass_rate
    ){
        onlineJudgeClientService.updateSolution(sid,result,time,memory,sim,sim_id,pass_rate);
        return new ResponseEntity("",HttpStatus.ACCEPTED);
    }


    @PostMapping("/addcompileerrorinformation/")
    public ResponseEntity<String> addCompileErrorInformation(
            @RequestParam(value="sid") String sid,
            @RequestParam(value="ceinfo") String ceinfo
    ){
        onlineJudgeClientService.addCompileErrorInformation(sid,ceinfo);
        return new ResponseEntity("",HttpStatus.ACCEPTED);
    }


    @PostMapping("/getsolution/")
    public ResponseEntity<String> getSolution(
            @RequestParam(value="sid") String sid
    ){
        String solution_content = onlineJudgeClientService.getSolution(sid);
        return new ResponseEntity(solution_content,HttpStatus.ACCEPTED);
    }


    /**
     * 该方法用于测试盘端的连通性,该部分的代码为其他部分的编写提供了样例
     * @param sid
     * @param max_running
     * @return 确保返回值一定是 HttpStatus.OK
     */
    @PostMapping("/test/")
    public ResponseEntity<String> test(
            @RequestParam(value="oj_lang_set") String sid,
            @RequestParam(value="max_running") String max_running
    ){
        System.out.println("---------This is the content---------------");
        System.out.println(sid);
        System.out.println(max_running);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hello");
        stringBuilder.append("\n");
        stringBuilder.append("world");
        stringBuilder.append("\n");
        return new ResponseEntity(stringBuilder.toString(),HttpStatus.OK);
    }
}
