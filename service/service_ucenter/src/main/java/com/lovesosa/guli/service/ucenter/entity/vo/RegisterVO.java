package com.lovesosa.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lovesosa
 */
@Data
public class RegisterVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nickname;
    private String mobile;
    private String password;
    private String code;
}
