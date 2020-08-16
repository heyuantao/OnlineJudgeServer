package cn.heyuantao.onlinejudgeserver.controller;

import cn.heyuantao.onlinejudgeserver.service.OnlineJudgeClientService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Api(tags={"判题机接口"})
@Controller
@RequestMapping("/api/v1/onlinejudgeclient/")
public class OnlineJudgeClientController {

    @Autowired
    OnlineJudgeClientService onlineJudgeClientService;

    /**
     * 返回待处理的题目编号
     * @return
     */
    @PostMapping("/getjobs/")
    public ResponseEntity<String> getJobs(){
        List<String> jobs = onlineJudgeClientService.getJobs(5);
        String content = listStringToMultiLineContent(jobs);
        return new ResponseEntity(content,HttpStatus.ACCEPTED);
    }


    /**
     * 更新某个题目的状态，每当题目的判题状态发生改变时会由判题机发出相应的请求
     * @param sid
     * @param result
     * @param time
     * @param memory
     * @param sim
     * @param sim_id
     * @param pass_rate
     * @return
     */
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


    /**
     * 将编译错误的信息存放起来
     * @param sid solution的编号
     * @param ceinfo  编译错误信息
     * @return
     */
    @PostMapping("/addcompileerrorinformation/")
    public ResponseEntity<String> addCompileErrorInformation(
            @RequestParam(value="sid") String sid,
            @RequestParam(value="ceinfo") String ceinfo
    ){
        onlineJudgeClientService.addCompileErrorInformation(sid,ceinfo);
        return new ResponseEntity("",HttpStatus.ACCEPTED);
    }


    /**
     * 获取某个Solution中用户提交的代码
     * @param sid
     * @return
     */
    @PostMapping("/getsolution/")
    public ResponseEntity<String> getSolution(
            @RequestParam(value="sid") String sid
    ){
        String solution_content = onlineJudgeClientService.getSolution(sid);
        return null;
        //return new ResponseEntity(solution_content,HttpStatus.ACCEPTED);
    }


    /**
     * 获取某个Solution中的其他信息，即问题的编号、用户名和语言的类型编号
     * @param sid
     * @return problem,username,lang
     */
    @PostMapping("/getsolutioninformation/")
    public ResponseEntity<String> getSolutionInformation(
            @RequestParam(value="sid") String sid
    ){
        return null;
    }


    /**
     * 获取某个Problem的相应信息
     * @param pid 问题的编号
     * @return String time_limit,String mem_limit,String isspj,
     */
    @PostMapping("/getprobleminformation/")
    public ResponseEntity<String> getProblemInformation(
            @RequestParam(value="pid") String pid
    ){
        return null;
    }


    /**
     * 处理程序运行时的错误信息
     * @param sid  Solution的编号
     * @param reinfo  运行的错误信息
     * @return 不返回任何内容
     */
    @PostMapping("/addruningerrorinformation/")
    public ResponseEntity<String> addRuningErrorInformation(
            @RequestParam(value = "sid") String sid,
            @RequestParam(value = "reinfo") String reinfo
    ){
        return null;
    }

    /**
     * 以列表的方式返回测试数据
     * @param pid
     * @return 返回内容如下
     * test01.in
     * test01.out
     * test02.in
     * test02.out
     */
    @PostMapping("/gettestdatalist/")
    public ResponseEntity<String> getTestDataList(
            @RequestParam(value = "pid") String pid
    ){
        List<String> testDataList = onlineJudgeClientService.getTestDataList(pid);
        String conent = listStringToMultiLineContent(testDataList);
        return new ResponseEntity(conent,HttpStatus.OK);
    }


    /**
     * 根据输入的文件名，返回对应的测试数据
     * @param filename 文件名
     * @return 测试数据的内容
     */
    @PostMapping("/gettestdatadata/")
    public ResponseEntity<String> getTestDataData(
            @RequestParam(value = "filename") String filename
    ){

        return null;
    }

    /**
     * 该接口仅仅用于提供对判题机原有接口的兼容性，实际并没有什么用途
     * 根据输入的文件名，返回测试文件的日期
     * @param filename
     * @return 日期（以字符串方式显示） 注意，必须以Linux下 stat.st_mtime的格式进行返回
     */
    @PostMapping("/gettestdatadate/")
    public ResponseEntity<String> getTestDataDate(
            @RequestParam(value = "filename") String filename
    ){
        String errorMessage = String.format("This api %s should not be called !","/gettestdatadate/");
        log.error(errorMessage);
        return new ResponseEntity<String>("",HttpStatus.OK);
    }

    /**
     * 该接口仅仅用于提供对判题机原有接口的兼容性，实际并没有什么用途
     * 该接口仅仅用于更新该题目作对的数量，实际中没什么用
     */
    @PostMapping("/updateprobleminformation/")
    public ResponseEntity<String> updateProblemInformation(
            @RequestParam(value = "pid") String pid
    ){
        String errorMessage = String.format("This api %s should not be called !","/updateprobleminformation/");
        log.error(errorMessage);
        return new ResponseEntity<String>("",HttpStatus.OK);
    }

    /**
     * 该接口仅仅用于提供对判题机原有接口的兼容性，实际并没有什么用途
     * 判题机端用该接口更新某个用户作对题目的数量
     * @return 返回空值
     */
    @PostMapping("/updateuserinformation/")
    public ResponseEntity<String> updateUserInformation(
            @RequestParam(value = "user_id") String user_id
    ){
        String errorMessage = String.format("This api %s should not be called !","/updateuserinformation/");
        log.error(errorMessage);
        return new ResponseEntity<String>("",HttpStatus.OK);
    }


    /**
     * 将List<String>的内容转成一个字符串并返回，字符串用换行符分割
     * @param list 每个元素为一个字符串
     * @return 返回一个字符串
     */
    private String listStringToMultiLineContent(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(String item:list){
            stringBuilder.append(item);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
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
