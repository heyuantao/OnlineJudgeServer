package cn.heyuantao.onlinejudgeserver.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author he_yu
 * 判题结果
 */
@AllArgsConstructor
@Data
@ToString
public class Result implements Serializable {
    private JudgeStatus judgeStatus;
    private double passRate;
    private String compileErrorInformation;
    private String runErrorInformation;


    /**
     * 默认初始化时，对应的状态为等待状态
     */
    public Result() {
        this.judgeStatus =JudgeStatus.PD;
        this.passRate = 0;
        this.compileErrorInformation = "";
        this.runErrorInformation = "";
    }

}
