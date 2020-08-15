package cn.heyuantao.onlinejudgeserver.domain;


import cn.heyuantao.onlinejudgeserver.core.Problem;
import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.validator.AllowString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author he_yu
 * 第三方客户端提交的数据
 */
public class ProblemRequestDTO implements Serializable {
    /**
     * 用户提交的代码
     */
    @NotEmpty(message = "源代码不能为空")
    @Length(min=1, max=1024, message = "代码长度不能为0且不能超过1024字节")
    private String sourceCode;

    /**
     * 语言的类型，用扩展名进行标识
     */
    @NotEmpty(message = "扩展名不能为空")
    @AllowString(message = "所填写的扩展名系统不支持",allows = {"C","CPP","JAVA"})
    private String langExtension;

    /**
     * 默认时间限制默认为1秒
     */
    @NotNull(message = "时间限制不能为空")
    private Integer timeLimit=1;

    /**
     * 默认内存限制为128M
     */
    @NotNull(message = "内存限制不能为空")
    private Integer memoryLimit=128;

    /**
     * 是否需要特殊判定,默认为False
     */
    private Boolean isSpecialJudge=Boolean.FALSE;

    /**
     * 针对代码的测试用例，至少有一个测试用例
     */
    @NotNull(message = "测试用例不能为空")
    private List<ProblemTestCase> testCaseList;

    @NotEmpty(message = "回调地址不能为空")
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
