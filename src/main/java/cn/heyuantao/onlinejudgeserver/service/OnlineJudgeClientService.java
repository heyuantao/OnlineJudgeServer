package cn.heyuantao.onlinejudgeserver.service;

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
     * 返回某个Soludion的程序代码
     * @param sid
     * @return
     */
    public String getSolution(String sid) {

        return "";
    }


}
