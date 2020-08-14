package cn.heyuantao.onlinejudgeserver.domain;


import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author he_yu
 * 第三方客户端提交的数据
 */
public class ProblemRequestDTO implements Serializable {
    @NotEmpty
    private String sourceCode;

    @NotEmpty
    private String langExtension;

    @NotNull
    private Integer timeLimit;

    @NotNull
    private Integer memoryLimit;

    @NotNull
    private Boolean isSpecialJudge;

    @NotNull
    private List<ProblemTestCase> testCaseList;

    @NotEmpty
    private String notifyAddress;

    public ProblemRequestDTO(String sourceCode, String langExtension, Integer timeLimit, Integer memoryLimit, Boolean isSpecialJudge, List<ProblemTestCase> testCaseList, String notifyAddress) {
        this.sourceCode = sourceCode;
        this.langExtension = langExtension;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.isSpecialJudge = isSpecialJudge;
        this.testCaseList = testCaseList;
        this.notifyAddress = notifyAddress;
    }

    public ProblemRequestDTO() {
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getLangExtension() {
        return langExtension;
    }

    public void setLangExtension(String langExtension) {
        this.langExtension = langExtension;
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

    public List<ProblemTestCase> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(List<ProblemTestCase> testCaseList) {
        this.testCaseList = testCaseList;
    }

    public String getNotifyAddress() {
        return notifyAddress;
    }

    public void setNotifyAddress(String notifyAddress) {
        this.notifyAddress = notifyAddress;
    }

    @Override
    public String toString() {
        return "ProblemRequestDTO{" +
                "sourceCode='" + sourceCode + '\'' +
                ", langExtension='" + langExtension + '\'' +
                ", timeLimit=" + timeLimit +
                ", memoryLimit=" + memoryLimit +
                ", isSpecialJudge=" + isSpecialJudge +
                ", testCaseList=" + testCaseList +
                ", notifyAddress='" + notifyAddress + '\'' +
                '}';
    }
}
