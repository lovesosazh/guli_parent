package com.lovesosa.guli.service.base.handler;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.ExceptionUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 全局异常处理
 * <p/>
 * @author lovesosa
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception exception) {
        log.error(ExceptionUtils.getMessage(exception));
        return R.error();
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public R error(BadSqlGrammarException exception) {
        log.error(ExceptionUtils.getMessage(exception));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R error(HttpMessageNotReadableException exception) {
        log.error(ExceptionUtils.getMessage(exception));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException exception) {
        log.error(ExceptionUtils.getMessage(exception));
        return R.error().message(exception.getMessage()).code(exception.getCode());
    }
}
