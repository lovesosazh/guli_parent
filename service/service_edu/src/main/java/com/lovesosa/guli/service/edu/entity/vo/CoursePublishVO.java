package com.lovesosa.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lovesosa
 */
@Data
public class CoursePublishVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectParentTitle;
    private String subjectTitle;
    private String teacherName;
    private String price;//只用于显示
}
