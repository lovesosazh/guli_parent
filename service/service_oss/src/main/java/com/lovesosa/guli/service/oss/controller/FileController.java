package com.lovesosa.guli.service.oss.controller;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.ExceptionUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lovesosa
 */
@Api(tags = "阿里云文件管理")
@Slf4j
@RestController
@RequestMapping("/admin/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(@ApiParam(value = "文件",required = true)
                    @RequestParam("file")
                    MultipartFile file,
                    @ApiParam(value = "模块",required = true)
                    @RequestParam("module")
                    String module) throws IOException {

        try {
            InputStream is = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(is, module, originalFilename);
            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("/remove")
    public R removeFile(@ApiParam(value = "要删除的文件的url路径",required = true)
                        @RequestBody String url) {
        fileService.removeFile(url);
        return R.ok().message("文件删除成功！");
    }


    /**
     * 远程调用测试接口
     */
    @ApiOperation("远程方法调用测试接口")
    @GetMapping("/test")
    public R test() {
        log.info("oos test被调用");
        return R.ok();
    }
}
