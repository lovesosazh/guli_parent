package com.lovesosa.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lovesosa
 */
@Data
public class WebCourseQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;
}
