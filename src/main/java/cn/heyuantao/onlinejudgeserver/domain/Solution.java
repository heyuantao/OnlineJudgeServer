package cn.heyuantao.onlinejudgeserver.domain;

import java.io.Serializable;

/**
 * @author he_yu
 * 表示题目的信息
 */
public class Solution implements Serializable {

    /**
     * 题目的编号
     */
    private String id;

    /**
     * 问题的信息
     */
    private Problem problem;

    /**
     * 判题状态
     */
    private Result result;


    public Solution(String id, Problem problem, Result result) {
        this.id = id;
        this.problem = problem;
        this.result = result;
    }

    public Solution() {
    }


    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id='" + id + '\'' +
                ", problem=" + problem +
                ", result=" + result +
                '}';
    }
}
