package cn.heyuantao.onlinejudgeserver.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author he_yu
 * 该异常仅仅用于传递信息
 */

public class MessageException extends RuntimeException{

    private String message;

    public MessageException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }

}
