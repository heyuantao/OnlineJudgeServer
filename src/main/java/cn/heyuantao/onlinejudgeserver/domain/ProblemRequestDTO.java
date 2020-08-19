package cn.heyuantao.onlinejudgeserver.domain;


import cn.heyuantao.onlinejudgeserver.core.ProblemTestCase;
import cn.heyuantao.onlinejudgeserver.annotation.AllowString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author he_yu
 * 第三方客户端提交的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    @AllowString(message = "所填写的扩展名系统不支持",allows = {"c","cpp","java"})
    private String langExtension;

    /**
     * 默认时间限制默认为1秒
     */
    @Range(min=1,max=10,message = "时间限制在1到10秒之间")
    private Integer timeLimit=1;

    /**
     * 默认内存限制为128M
     */
    @Range(min=8,max=256,message = "内存限制在8-256兆之间")
    private Integer memoryLimit=128;

    /**
     * 是否需要特殊判定,默认为False
     */
    private Boolean isSpecialJudge=Boolean.FALSE;

    /**
     * 针对代码的测试用例，测试用例可以为空
     */
    @NotNull(message = "测试用例不能为空")
    private List<ProblemTestCase> testCaseList;

    /**
     * 系统的回调地址，用该地址提供通知
     */
    @NotEmpty(message = "回调地址不能为空")
    @NotNull(message = "回调地址必须提供")
    private String notifyAddress;

}
