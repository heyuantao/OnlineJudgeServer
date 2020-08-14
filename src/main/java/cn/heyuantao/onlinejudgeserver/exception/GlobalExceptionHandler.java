package cn.heyuantao.onlinejudgeserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author he_yu
 * 全局异常处理函数
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理，处理未知的错误
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        exception.printStackTrace(printWriter);
        log.error("--------------------Exception Info Begin-------------------------");
        log.error(stringWriter.toString());
        log.error("--------------------Exception Info End---------------------------");

        ErrorDetails errorDetails = new ErrorDetails("发现错误", exception.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 全局异常处理，处理接口层面的数据异常的错误
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(BindingResultException.class)
    public ResponseEntity<?> handleBindingResultException(BindingResultException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),"Request 数据校验错误");
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * 方法不支持的异常，当对模型接口发送了不被支持的方法时候会触发该异常
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails("接口不支持该方法", exception.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }


}
