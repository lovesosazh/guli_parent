package com.lovesosa.guli.service.oss.service;

import java.io.InputStream;

/**
 * @author lovesosa
 */
public interface FileService {

    /**
     * 阿里云oss文件上传
     * @param inputStream 输入流
     * @param module 文件夹名称
     * @param originalFileName 原始文件名字
     * @return 文件在oss服务器的url地址
     */
    String upload(InputStream inputStream, String module,String originalFileName);


    /**
     * 阿里云oss 文件删除
     * @param url
     */
    void removeFile(String url);
}
