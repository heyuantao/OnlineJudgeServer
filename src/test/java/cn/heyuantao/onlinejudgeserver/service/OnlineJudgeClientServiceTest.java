package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class OnlineJudgeClientServiceTest {

    @Autowired
    RedisService redisService;

    @BeforeEach
    public void prepare(){
        String solutionId = "XXX1778a996a425ba1d41096a0215XXX";

        System.out.println("prepare data");
        Solution solution = new Solution();
        solution.setId(solutionId);

        Problem problem = new Problem();
        List<ProblemTestCase> problemTestCaseList = new ArrayList<ProblemTestCase>();

        ProblemTestCase testCase;
        testCase = new ProblemTestCase();
        testCase.setInput("121212");
        testCase.setTarget("232323");
        problemTestCaseList.add(testCase);
        testCase = new ProblemTestCase();
        testCase.setInput("abcef");
        testCase.setTarget("retertret");
        problemTestCaseList.add(testCase);

        problem.setTestCaseList(problemTestCaseList);

        solution.setProblem(problem);

        redisService.insertSolutionIntoRedis(solution);
    }

    @Test
    public void getTestFileByName() {
        String solutionId = "XXX1778a996a425ba1d41096a0215XXX";
        String testFilename = "test1.in";

        /**
         * 首先提取出文件中的数字，确保找到正确的测试数据
         */
        String nameWithOutTest = StringUtils.removeStartIgnoreCase(testFilename,"test");
        String nameWithDigtal = null;
        nameWithDigtal = StringUtils.removeEndIgnoreCase(nameWithOutTest,".in");
        nameWithDigtal = StringUtils.removeEndIgnoreCase(nameWithDigtal,".out");
        System.out.println(nameWithDigtal);
        Integer index = Integer.parseInt(nameWithDigtal);

        /**
         * 获取Solution中的TestCase
         */
        Solution solution = redisService.getSolutionById(solutionId);
        List<ProblemTestCase> testCaseList = solution.getProblem().getTestCaseList();

        if( index > (testCaseList.size()-1) ){
            String errorMessage = String.format("索引为 '%s' 的测试数据不存在",index);
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }

        /**
         * 获取当前的测试用例
         */
        ProblemTestCase testCase = testCaseList.get(index);

        /**
         * 其次查找是输入还是输出文件
         */
        if(StringUtils.endsWithIgnoreCase(testFilename,".in")){
            System.out.println(testCase.getInput());
        }else if(StringUtils.endsWithIgnoreCase(testFilename,".out")){
            System.out.println(testCase.getTarget());
        }else{
            String errorMessage = String.format("Unsupport testfile name %s !",testFilename);
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }
    }

    @Test
    void testForInitContainerSize() {
        List<String> stringList = null;
        stringList = new ArrayList<String>();
        for(Integer i=0;i<20;i++){
            stringList.add(i.toString());
        }
        System.out.println(stringList.size());
        stringList = new ArrayList<String>(10);
        for(Integer i=0;i<20;i++){
            stringList.add(i.toString());
        }
        System.out.println(stringList.size());
    }
}