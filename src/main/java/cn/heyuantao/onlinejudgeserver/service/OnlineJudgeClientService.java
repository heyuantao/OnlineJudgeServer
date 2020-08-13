package cn.heyuantao.onlinejudgeserver.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 * 将用户的数据存储在Redis中
 */
@Service
public class OnlineJudgeClientService {

    public List<String> getJobs() {
        List<String> list=new ArrayList<>();
        list.add("121212");
        list.add("2223232");
        return list;
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
