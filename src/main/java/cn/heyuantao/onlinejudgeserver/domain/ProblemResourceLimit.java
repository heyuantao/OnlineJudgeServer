package cn.heyuantao.onlinejudgeserver.domain;

import io.swagger.models.auth.In;

import java.io.Serializable;

/**
 * @author he_yu
 * 题目的资源限制
 */
public class ProblemResourceLimit implements Serializable {
    private Integer timeLimit;
    private Integer memoryLimit;
    private Boolean isSpecialJudge;

    public ProblemResourceLimit(Integer timeLimit, Integer memoryLimit, Boolean isSpecialJudge) {
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.isSpecialJudge = isSpecialJudge;
    }

    /**
     * 默认的代码运行资源限制和其他要求
     */
    public ProblemResourceLimit() {
        this.timeLimit = 10;
        this.memoryLimit = 512;
        this.isSpecialJudge = Boolean.FALSE;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Boolean getSpecialJudge() {
        return isSpecialJudge;
    }

    public void setSpecialJudge(Boolean specialJudge) {
        isSpecialJudge = specialJudge;
    }

    @Override
    public String toString() {
        return "ProblemResourceLimit{" +
                "timeLimit=" + timeLimit +
                ", memoryLimit=" + memoryLimit +
                ", isSpecialJudge=" + isSpecialJudge +
                '}';
    }
}
