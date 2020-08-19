package cn.heyuantao.onlinejudgeserver.annotation;


import java.lang.annotation.*;

/**
 * 当配置为value=false时，不执行调试程序
 * @author he_yu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Debug {
    String value() default "false";
}
