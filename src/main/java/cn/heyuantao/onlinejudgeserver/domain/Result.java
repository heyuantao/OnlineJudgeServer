package cn.heyuantao.onlinejudgeserver.domain;



/**
 * @author he_yu
 * 判题结果
 */
public class Result {
    private JudgeStatus judgeStatus;
    private double passRate;

    public Result(JudgeStatus judgeStatus, double passRate) {
        this.judgeStatus = judgeStatus;
        this.passRate = passRate;
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
}
