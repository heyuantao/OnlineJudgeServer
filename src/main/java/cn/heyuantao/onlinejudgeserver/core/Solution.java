package cn.heyuantao.onlinejudgeserver.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author he_yu
 * 表示题目的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

}
