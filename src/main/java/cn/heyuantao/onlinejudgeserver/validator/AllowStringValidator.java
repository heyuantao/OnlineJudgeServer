package cn.heyuantao.onlinejudgeserver.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于字符的验证
 * @author he_yu
 */
public class AllowStringValidator implements ConstraintValidator<AllowString,String> {

    private List<String> stringList;

    @Override
    public void initialize(AllowString constraintAnnotation) {
        stringList = Arrays.stream(constraintAnnotation.allows()).collect(Collectors.toList());
        //System.out.println(stringList);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        for(String str:stringList){
            if(StringUtils.equals(str,s)){
                return true;
            }
        }
        return false;
    }
}
