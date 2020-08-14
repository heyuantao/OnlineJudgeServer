package cn.heyuantao.onlinejudgeserver.domain;

import java.util.List;

/**
 * @author he_yu
 * 要判定的题目
 */
public class Problem {
    private String sourceCode;
    private List<ProblemTestCase> testCaseList;
    private LanguageType languageType;
    private ProblemResourceLimit problemResourceLimit;
}
