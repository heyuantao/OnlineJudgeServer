package cn.heyuantao.onlinejudgeserver.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author he_yu
 * 要判定的题目
 */
public class Problem implements Serializable {
    private String sourceCode;
    private List<ProblemTestCase> testCaseList;
    private LanguageType languageType;
    private ProblemResourceLimit problemResourceLimit;
    private String notifyAddress;


    public Problem(String sourceCode, List<ProblemTestCase> testCaseList, LanguageType languageType, ProblemResourceLimit problemResourceLimit, String notifyAddress) {
        this.sourceCode = sourceCode;
        this.testCaseList = testCaseList;
        this.languageType = languageType;
        this.problemResourceLimit = problemResourceLimit;
        this.notifyAddress = notifyAddress;
    }

    /**
     * 默认的初始化函数
     */
    public Problem() {
        this.sourceCode="";
        this.testCaseList = new ArrayList<ProblemTestCase>();
        this.languageType = LanguageType.C;
        this.problemResourceLimit = new ProblemResourceLimit();
        this.notifyAddress = "";
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public List<ProblemTestCase> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(List<ProblemTestCase> testCaseList) {
        this.testCaseList = testCaseList;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public ProblemResourceLimit getProblemResourceLimit() {
        return problemResourceLimit;
    }

    public void setProblemResourceLimit(ProblemResourceLimit problemResourceLimit) {
        this.problemResourceLimit = problemResourceLimit;
    }

    public String getNotifyAddress() {
        return notifyAddress;
    }

    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "sourceCode='" + sourceCode + '\'' +
                ", testCaseList=" + testCaseList +
                ", languageType=" + languageType +
                ", problemResourceLimit=" + problemResourceLimit +
                ", notifyAddress='" + notifyAddress + '\'' +
                '}';
    }
}
