package cn.heyuantao.onlinejudgeserver.domain;

import io.swagger.models.auth.In;

/**
 * @author he_yu
 * 题目的资源限制
 */
public class ProblemResourceLimit {
    private Integer timeLimit;
    private Integer memoryLimit;
    private Boolean isSpecialJudge;

    public ProblemResourceLimit(Integer timeLimit, Integer memoryLimit, Boolean isSpecialJudge) {
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.isSpecialJudge = isSpecialJudge;
    }

    public ProblemResourceLimit() {
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
}
