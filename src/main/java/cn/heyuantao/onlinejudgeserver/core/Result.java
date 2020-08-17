package cn.heyuantao.onlinejudgeserver.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author he_yu
 * 判题结果,即对应的题目的运行结果
 */
@AllArgsConstructor
@Data
@ToString
public class Result implements Serializable {

    /**
     * JudgeStatus(判题状态)，time(判题耗费时间)、memory(判题耗费的内存)
     * sim(不清楚)、simID(不清楚)
     * passRate(判题正确率)
     * compileErrorInformation(编译错误的提示信息)
     * runErrorInformation（运行错误的提示信息)
     */
    private JudgeStatus judgeStatus;
    private String time;
    private String memory;
    private String sim;
    private String simId;
    private double passRate;
    private String compileErrorInformation;
    private String runErrorInformation;


    /**
     * 默认初始化时，对应的状态为等待状态，正确率初始化为零
     * 其他信息都初始化为空
     */
    public Result() {
        this.judgeStatus = JudgeStatus.PD;
        this.time = "";
        this.memory = "";
        this.sim = "";
        this.simId = "";
        this.passRate = 0;
        this.compileErrorInformation = "";
        this.runErrorInformation = "";
    }

}
