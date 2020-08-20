package cn.heyuantao.onlinejudgeserver.annotation;


import java.lang.annotation.*;

/**
 * 使用时设置Debug(value=?),其中value设置的值为"true","false","on","off"
 * 当value为true和on是为调试模式，为false、off或者其他值时为非调试模式
 * @author he_yu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Debug {
    String value() default "false";
}
