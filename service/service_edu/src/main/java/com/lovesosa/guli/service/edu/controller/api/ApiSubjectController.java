package com.lovesosa.guli.service.edu.controller.api;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.entity.vo.SubjectVO;
import com.lovesosa.guli.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lovesosa
 */
@Api(tags="课程分类")
@RestController
@RequestMapping("/api/edu/subject")
public class ApiSubjectController {

    @Autowired
    private ISubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList() {
        List<SubjectVO> subjectVOList = subjectService.nestedList();
        return R.ok().data("items",subjectVOList);
    }
}
