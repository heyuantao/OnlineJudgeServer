package cn.heyuantao.onlinejudgeserver.service;

import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.core.Solution;
import cn.heyuantao.onlinejudgeserver.exception.MessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 * 将用户的数据存储在Redis中
 */
@Service
public class OnlineJudgeClientService {

    @Autowired
    RedisService redisService;


    /**
     * 从队列中返回几个任务，并确保返回任务数量不超过maxJobs
     */
    public List<String> getJobs(Integer maxJobs){
        List<String> list = new ArrayList<String>();
        Integer retryCount =0;
        for(;;){
            String id = redisService.pickOneSolutionAndPutIntoProcessingQueue();
            if(id!=null){
                list.add(id);
            }else{
                retryCount = retryCount +1;
            }
            if( (retryCount>2)||(list.size()>maxJobs) ){
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
     * @param id
     * @return
     */
    public List<String> getTestDataList(String id){
        List<String> stringList = new ArrayList<>();

        Solution solution = redisService.getSolutionById(id);
        if(solution==null){
            throw new MessageException("Can not find solution "+id);
        }

        List<ProblemTestCase> testCaseList = solution.getProblem().getTestCaseList();
        for(int i=0;i<testCaseList.size();i++){
            String testInFilename = String.format("test%d.in",i);
            String testOutFileName = String.format("test%d.out",i);
            stringList.add(testInFilename);
            stringList.add(testOutFileName);
        }
        return stringList;
    }

    /**
     * 返回某个Soludion的程序代码
     * @param sid
     * @return
     */
    public String getSolution(String sid) {

        return "";
    }


}
