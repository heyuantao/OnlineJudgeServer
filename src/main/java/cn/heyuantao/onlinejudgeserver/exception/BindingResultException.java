package cn.heyuantao.onlinejudgeserver.exception;

import org.springframework.validation.BindingResult;

/**
 * @author he_yu
 * 接口字段验证错误
 */
public class BindingResultException extends RuntimeException  {
    /**
     * This is validate result from controller
     */
    private BindingResult bindingResult;

    public BindingResultException(BindingResult bindingResult){
        this.bindingResult=bindingResult;
    }

    @Override
    public String getMessage() {
        return this.bindingResult.getFieldError().getDefaultMessage();
    }
}
