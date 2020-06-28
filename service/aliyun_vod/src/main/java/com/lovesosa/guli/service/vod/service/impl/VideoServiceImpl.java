package com.lovesosa.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.vod.service.VideoService;
import com.lovesosa.guli.service.vod.util.AliyunVodSdkUtils;
import com.lovesosa.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @author lovesosa
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream file, String originalFilename) {
        String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String keyid = vodProperties.getKeyid();
        String keysecret = vodProperties.getKeysecret();


        UploadStreamRequest request = new UploadStreamRequest(keyid, keysecret, title, originalFilename, file);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);


        String videoId = response.getVideoId();
        if (StringUtils.isEmpty(videoId)) {
            log.error("阿里云上传失败: " + response.getCode() + "-" + response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        String keyid = vodProperties.getKeyid();
        String keysecret = vodProperties.getKeysecret();
        DefaultAcsClient client = AliyunVodSdkUtils.initVodClient(keyid, keysecret);

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {

        //初始化client对象
        DefaultAcsClient client = AliyunVodSdkUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        DeleteVideoRequest request = new DeleteVideoRequest();

        int size = videoIdList.size();
        StringBuffer idListStr = new StringBuffer();
        for (int i = 0; i < size; i++) {

            idListStr.append(videoIdList.get(i));
            if(i == size -1 || i % 20 == 19){
                log.info("idListStr = " + idListStr.toString());
                //支持传入多个视频ID，多个用逗号分隔，最多20个
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                idListStr = new StringBuffer();
            }else if(i % 20 < 19){
                idListStr.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSdkUtils.initVodClient(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret());

        //创建请求对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest ();
        request.setVideoId(videoSourceId);

        //获取响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        return response.getPlayAuth();
    }


}
