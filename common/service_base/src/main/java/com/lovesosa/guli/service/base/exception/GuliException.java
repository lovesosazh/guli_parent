package com.lovesosa.guli.service.base.exception;

import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author lovesosa
 */
@Data
public class GuliException extends RuntimeException {

    static final long serialVersionUID = 1L;

    private Integer code;

    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }


    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                " message=" + this.getMessage() +
                '}';
    }
}
