package com.zxy.exception;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result resultError(Exception e){
    return new Result(true, StatusCode.ERROR,e.getMessage() );
    }
}
