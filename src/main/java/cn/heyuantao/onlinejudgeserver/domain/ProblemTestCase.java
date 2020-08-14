package cn.heyuantao.onlinejudgeserver.domain;

import java.io.Serializable;

/**
 * @author he_yu
 * 每个题目的测试用例，包含了输入的内容和输出的内容
 */
public class ProblemTestCase implements Serializable {
    /**
     * 输入的内容
     */
    private String input;

    /**
     * 预期的输出
     */
    private String target;


    public ProblemTestCase(String input, String target) {
        this.input = input;
        this.target = target;
    }

    public ProblemTestCase() {
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "ProblemTestCase{" +
                "input='" + input + '\'' +
                ", target='" + target + '\'' +
                '}';
    }
}
