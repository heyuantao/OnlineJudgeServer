package cn.heyuantao.onlinejudgeserver.exception;

/**
 * @author he_yu
 * 软件内部定义的异常，通常发生在要查找的某个值不存在时发生
 * 发生这种错误的原因是要查找的值不存在，此时需要查找该值的设置，看看是否要查找的值设置的有错误
 */
public class InvalidValueException extends RuntimeException {

    private String message;

    public InvalidValueException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        String errorMessage = String.format("请检查数值的设置:%s.",this.message);
        return errorMessage;
    }
}
