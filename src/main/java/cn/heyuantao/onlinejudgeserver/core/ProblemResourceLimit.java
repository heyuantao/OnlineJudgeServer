package cn.heyuantao.onlinejudgeserver.core;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author he_yu
 * 题目的资源限制
 */
@Data
@ToString
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
        this.timeLimit = 2;
        this.memoryLimit = 128;
        this.isSpecialJudge = Boolean.FALSE;
    }

}
