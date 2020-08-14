package cn.heyuantao.onlinejudgeserver.core;


import java.io.Serializable;

/**
 * @author he_yu
 * 判题结果
 */
public class Result implements Serializable {
    private JudgeStatus judgeStatus;
    private double passRate;

    public Result(JudgeStatus judgeStatus, double passRate) {
        this.judgeStatus = judgeStatus;
        this.passRate = passRate;
    }

    /**
     * 默认初始化时，对应的状态为等待状态
     */
    public Result() {
        this.judgeStatus =JudgeStatus.PD;
        this.passRate = 0;
    }

    public JudgeStatus getJudgeStatus() {
        return judgeStatus;
    }

    public void setJudgeStatus(JudgeStatus judgeStatus) {
        this.judgeStatus = judgeStatus;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    @Override
    public String toString() {
        return "Result{" +
                "judgeStatus=" + judgeStatus +
                ", passRate=" + passRate +
                '}';
    }
}
