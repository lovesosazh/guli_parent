package com.lovesosa.guli.service.edu.controller.admin;


import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.ExceptionUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.edu.entity.vo.SubjectVO;
import com.lovesosa.guli.service.edu.service.ISubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class SubjectController {

    @Autowired
    private ISubjectService subjectService;

    @ApiOperation("Excel批量导入课程分类")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "上传Excel的文件",required = true)
            @RequestParam("file")
            MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("嵌套数据列表")
    @GetMapping("/nested-list")
    public R nestedList() {
        List<SubjectVO> subjectVOS = subjectService.nestedList();
        return R.ok().data("items",subjectVOS);
    }
}

