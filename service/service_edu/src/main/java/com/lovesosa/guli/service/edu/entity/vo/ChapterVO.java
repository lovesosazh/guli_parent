package com.lovesosa.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lovesosa
 */
@Data
public class ChapterVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private Integer sort;
    private List<VideoVO> children = new ArrayList<>();
}
