package com.lovesosa.guli.service.cms.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lovesosa
 */
@Data
public class AdVO implements Serializable {

    private static final long serialVersionUID=1L;
    private String id;
    private String title;
    private Integer sort;
    private String type;
}
