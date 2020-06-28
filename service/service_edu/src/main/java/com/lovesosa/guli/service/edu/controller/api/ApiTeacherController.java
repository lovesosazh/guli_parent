package com.lovesosa.guli.service.edu.controller.api;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.entity.Teacher;
import com.lovesosa.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author lovesosa
 */
@Api(tags="讲师")
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private ITeacherService teacherService;

    @ApiOperation(value="所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items", list).message("获取讲师列表成功");
    }


    @ApiOperation(value = "获取讲师")
    @GetMapping("get/{id}")
    public R get(
            @ApiParam(value = "讲师ID", required = true)
            @PathVariable String id) {
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map);
    }
}
