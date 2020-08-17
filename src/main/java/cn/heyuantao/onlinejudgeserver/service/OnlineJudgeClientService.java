package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 * 将用户的数据存储在Redis中
 */
@Slf4j
@Service
public class OnlineJudgeClientService {

    @Autowired
    RedisService redisService;


    /**
     * 从队列中返回几个任务，并确保返回任务数量不超过maxJobs
     */
    public List<String> getJobs(Integer maxJobs) {
        List<String> list = new ArrayList<String>();
        Integer retryCount = 0;
        for (; ; ) {
            String id = redisService.pickOneSolutionAndPutIntoProcessingQueue();
            if (id != null) {
                list.add(id);
            } else {
                retryCount = retryCount + 1;
            }
            if ((retryCount > 2) || (list.size() > maxJobs)) {
                return list;
            }
        }
    }

    public void updateSolution(String sid, String result, String time, String memory, String sim, String sim_id, String pass_rate) {

    }

    public void addCompileErrorInformation(String sid, String ceinfo) {

    }


    /**
     * 根据Solution的ID，生成输入和输出的文件对，样例如下
     * ##################
     * test1.in
     * test1.out
     * test2.in
     * test2.out
     * ################
     *
     * @param id
     * @return
     */
    public List<String> getTestDataList(String id) {
        List<String> stringList = new ArrayList<>();

        Solution solution = redisService.getSolutionById(id);
        if (solution == null) {
            throw new MessageException("Can not find solution " + id);
        }

        List<ProblemTestCase> testCaseList = solution.getProblem().getTestCaseList();
        for (int i = 0; i < testCaseList.size(); i++) {
            String testInFilename = String.format("test%d.in", i);
            String testOutFileName = String.format("test%d.out", i);
            stringList.add(testInFilename);
            stringList.add(testOutFileName);
        }
        return stringList;
    }


    /**
     * 根据SolutionId和testFilename来获取相应的测试数据,在控制器层面以"soludionId/testFilename"的形式进行请求，
     * 在服务层面将这两部分进行分开
     *
     * @param solutionId   类似的形式为"23423423423"
     * @param testFilename 类似的形式为test0.in，test0.out,test1.in,test1.out
     * @return
     */
    public String getTestFileByName(String solutionId, String testFilename) {

        /**
         * 检查文件名是否正确，该部分之应该包含不带路径的文件名，且以.in或.out结束
         */
        if (StringUtils.contains(testFilename, "/")) {
            String errorMessage = String.format("文件的名字不能包含 %s 字符", "/");
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }
        if ((!StringUtils.endsWith(testFilename, ".in")) && (!(StringUtils.endsWith(testFilename, ".out")))) {
            String errorMessage = String.format("文件的名字不能包含必须以%s 或 %s 结束", ".in", ".out");
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }

        /**
         * 首先提取出文件中的数字，确保找到正确的测试数据
         */
        String nameWithOutTest = StringUtils.removeIgnoreCase(testFilename, "test");
        String nameWithDigtal = null;
        nameWithDigtal = StringUtils.removeEndIgnoreCase(nameWithOutTest, ".in");
        nameWithDigtal = StringUtils.removeEndIgnoreCase(nameWithDigtal, ".out");
        Integer index = Integer.parseInt(nameWithDigtal);

        /**
         * 获取Solution中的TestCase
         */
        Solution solution = redisService.getSolutionById(solutionId);
        List<ProblemTestCase> testCaseList = solution.getProblem().getTestCaseList();

        if (index > (testCaseList.size() - 1)) {
            String errorMessage = String.format("索引为 '%s' 的测试数据不存在", index);
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
        if (StringUtils.endsWithIgnoreCase(testFilename, ".in")) {
            return testCase.getInput();
        } else if (StringUtils.endsWithIgnoreCase(testFilename, ".out")) {
            return testCase.getTarget();
        } else {
            String errorMessage = String.format("Unsupport testfile name %s !", testFilename);
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }
    }

    /**
     * 获取对应题目的源代码
     * @param sid
     * @return
     */
    public String getSolutionSourceCode(String sid){
        Solution solution = redisService.getSolutionById(sid);
        String sourceCode =  solution.getProblem().getSourceCode();
        if( (sourceCode==null) || (StringUtils.length(sourceCode)==0)){
            String errorMessage = String.format("Solution %s source code is empty !",sid);
            log.error(errorMessage);
            throw new MessageException(errorMessage);
        }
        return sourceCode;
    }

    /**
     * 获取对应题目的语言类型
     * @param sid
     * @return
     */
    public Integer getSolutionLang(String sid){
        Solution solution = redisService.getSolutionById(sid);
        Integer langTypeId =  solution.getProblem().getLanguageType().getValue();
        return langTypeId;
    }
}
