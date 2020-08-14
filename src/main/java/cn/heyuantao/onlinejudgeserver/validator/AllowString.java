package cn.heyuantao.onlinejudgeserver.validator;

import org.springframework.core.annotation.AliasFor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author he_yu
 * 允许出现的字符串
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AllowStringValidator.class )
public @interface AllowString {
    String message() default "扩展名字段验证错误";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @AliasFor("path")
    String[] allows() default {};

}
