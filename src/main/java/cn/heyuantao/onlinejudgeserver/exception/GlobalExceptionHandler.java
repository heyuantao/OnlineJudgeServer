package cn.heyuantao.onlinejudgeserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * @author he_yu
 * 全局异常处理函数
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理，处理未知的错误。在处理异常的时候同时将堆栈信息打印到日志中
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
     * 全局异常处理，处理数据校验的错误
     * 其中该异常的全局处理需要在Controller的类上写上@Validated这个注解
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleBindingResultException(ConstraintViolationException exception, WebRequest request){
        Set<ConstraintViolation<?>> constraintViolationSet = exception.getConstraintViolations();
        /**
         * 可能同时会有多个校验出错的信息，但只显示一个
         */
        if(constraintViolationSet.size()>0){
            String defaultMesage = constraintViolationSet.iterator().next().getMessage();
            ErrorDetails errorDetails = new ErrorDetails("数据校验错误",defaultMesage);
            return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
        }else{
            String errorMessage = "至少要有一个校验错误，但当前一个也没有找到，软件可能存在问题！";
            log.error(errorMessage);
            ErrorDetails errorDetails = new ErrorDetails("参数校验异常信息出现错误",errorMessage);
            return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 在Controller使用POJO来验证时，会手动抛出验证错误的异常，并由如下函数处理
     */
    @ExceptionHandler(BindingResultException.class)
    public ResponseEntity<?> handleBindingResultException(BindingResultException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(),"Request 数据校验错误");
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }


    /**
     * 方法不支持的异常，当对模型接口发送了不被支持的方法时候会触发该异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails("接口不支持该方法", exception.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }


}
