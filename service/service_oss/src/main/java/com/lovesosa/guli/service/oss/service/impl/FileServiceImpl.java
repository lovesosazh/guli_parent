package com.lovesosa.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.lovesosa.guli.service.oss.service.FileService;
import com.lovesosa.guli.service.oss.util.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author lovesosa
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String originalFileName) {

        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        if (!ossClient.doesBucketExist(bucketname)) {
            ossClient.createBucket(bucketname);
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }


        String fileName = UUID.randomUUID().toString();
        String folder = new DateTime().toString("yyyy/MM/dd");
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String objectName = module + "/" + folder + "/" + fileName + fileExtension;

        // 上传文件流。
        ossClient.putObject(bucketname, objectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        // 返回url
        String url = "https://" + bucketname + "." + endpoint + "/" + objectName;
        return url;
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyid();
        String keysecret = ossProperties.getKeysecret();
        String bucketname = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        // 删除文件。
        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        ossClient.deleteObject(bucketname, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
