package com.lovesosa.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.edu.entity.Teacher;
import com.lovesosa.guli.service.edu.entity.vo.TeacherQueryVO;
import com.lovesosa.guli.service.edu.feign.OssFileService;
import com.lovesosa.guli.service.edu.service.ITeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-05-25
 */
@Api(tags = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private OssFileService ossFileService;

    @ApiOperation("所有讲师列表")
    @GetMapping("/list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items",list);
    }


    @ApiOperation("根据id删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@PathVariable("id") String id) {
        // 删除讲师头像
        teacherService.removeAvatarById(id);

        boolean result = teacherService.removeById(id);
        if (result) {
            return R.ok().message("删除成功！");
        } else {
            return R.error().message("删除失败！");
        }

    }

    @ApiOperation("讲师分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码",required = true) @PathVariable("page") Long page,
                      @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVO teacherQueryVO) {

        Page<Teacher> teacherPage = new Page<>(page, limit);
        IPage<Teacher> pageModel = teacherService.selectPage(teacherPage, teacherQueryVO);
        List<Teacher> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }


    @ApiOperation("新增讲师")
    @PostMapping("/save")
    public R save(@ApiParam("讲师对象") @RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return R.ok().message("保存成功!");
    }

    @ApiOperation("更新讲师")
    @PutMapping("/update")
    public R updateById(@ApiParam("讲师对象") @RequestBody Teacher teacher) {
        boolean result = teacherService.updateById(teacher);
        if (result) {
            return R.ok().message("修改成功!");
        } else {
            return R.error().message("数据不存在!");
        }
    }

    @ApiOperation("根据id获取讲师信息")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam("讲师id") @PathVariable("id") String id) {
        Teacher teacher = teacherService.getById(id);

        if (teacher != null) {
            return R.ok().data("item",teacher);
        } else {
            return R.error().message("数据不存在！");
        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeRows(@ApiParam(value = "讲师id列表",required = true)
                        @RequestBody List<String> ids) {
        boolean result = teacherService.removeByIds(ids);
        if (result) {
            return R.ok().message("删除成功！");
        } else {
            return R.error().message("删除失败！");
        }

    }


    @ApiOperation("根据关键字查询讲师列表")
    @GetMapping("/list/name/{key}")
    public R selectNameByKey(@ApiParam(value = "关键字") @PathVariable String key) {
        List<Map<String, Object>> maps = teacherService.selectNameList(key);
        return R.ok().data("nameList",maps);
    }



    @ApiOperation("测试调用OSS远程方法")
    @GetMapping("/test")
    public R testFeign() {
//        ossFileService.test();
        log.info("..............test");
        ossFileService.test();
        return R.ok();
    }



    @ApiOperation("测试并发访问")
    @GetMapping("/testcocurrent")
    public R testConcurrent() {
        log.info("..............test_current");
        return R.ok();
    }

    @ApiOperation("message1")
    @GetMapping("/message1")
    public String message1() {
        return "message1";
    }

    @ApiOperation("message2")
    @GetMapping("/message2")
    public String message2() {
        return "message2";
    }
}

