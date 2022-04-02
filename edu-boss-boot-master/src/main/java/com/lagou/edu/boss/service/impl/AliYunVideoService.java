package com.lagou.edu.boss.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyRequest;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.lagou.edu.boss.service.AliyunTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Bobbao
 * @description
 * @date 2019-08-20 21:21
 */
@Slf4j
@Service
public class AliYunVideoService {

    //账号AK信息请填写(必选)
    private  static final String ACCESS_KEY_ID ="LTAI4GEF383H2nAUGdZ6MzDX";
    //账号AK信息请填写(必选)
    private  static final String ACCESS_KEY_SECRET = "2qpeWdVmuCRv7cdCuWU3wmDCoQYtNI";

    public static final String VIDEO_TRANSCODE = "a56bc98cf4405c6529d6b5e0b7423758";

    private  static DefaultAcsClient aliClient = initVodClient(ACCESS_KEY_ID,ACCESS_KEY_SECRET) ;
    @Autowired
    private  RedisTemplate redisTemplate;

    public  static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 生成视频上传地址和凭证
     *
     * @param fileName
     * @param imageUrl
     * @return
     * @throws ClientException
     */
    public  CreateUploadVideoResponse generateVideoUploadAddressAndAuth(String fileName, String imageUrl) throws ClientException {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(fileName);
        request.setFileName(fileName);
        request.setCoverURL(imageUrl);

        JSONObject userData = new JSONObject();
        JSONObject messageCallback = new JSONObject();
        messageCallback.put("CallbackType", "https");
        userData.put("MessageCallback", messageCallback.toJSONString());

        request.setUserData(userData.toString());
        CreateUploadVideoResponse acsResponse = aliClient.getAcsResponse(request);
        log.info("获取到上传地址信息:vid:" + acsResponse.getVideoId() + ", url:" + acsResponse.getUploadAddress() + ", 上传凭证:" + acsResponse.getUploadAuth());

        return acsResponse;
    }

    /**
     * 刷新视频上传地址和凭证
     *
     * @param videoId
     * @return
     * @throws Exception
     */
    public  RefreshUploadVideoResponse refreshUploadVideo(String videoId) throws Exception {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(videoId);
        return aliClient.getAcsResponse(request);
    }




    /**
     * 获取图片上传地址和凭证
     *
     * @return CreateUploadImageResponse 获取图片上传地址和凭证响应数据
     * @throws Exception
     */
    public  CreateUploadImageResponse generateAliyunImagUploadAddressAdnAuth() throws Exception {
        CreateUploadImageRequest request = new CreateUploadImageRequest();
        request.setImageType("cover");
        request.setImageExt("png");
//        JSONObject userData = new JSONObject();
//        JSONObject messageCallback = new JSONObject();
//        messageCallback.put("CallbackURL", "http://xxxxx");
//        messageCallback.put("CallbackType", "http");
//        userData.put("MessageCallback", messageCallback.toJSONString());
//        JSONObject extend = new JSONObject();
//        extend.put("MyId", "user-defined-id");
//        userData.put("Extend", extend.toJSONString());
//        request.setUserData(userData.toJSONString());
        return aliClient.getAcsResponse(request);
    }

    /**
     * 转码请求
     *
     * @return
     * @throws ClientException
     */
    public  SubmitTranscodeJobsResponse transcodeVideo(AliyunTask aliyunTask, String transCode) throws ClientException {
        SubmitTranscodeJobsRequest request = new SubmitTranscodeJobsRequest();
        //需要转码的视频ID
        request.setVideoId(aliyunTask.getFileId());
        //转码模板ID
        request.setTemplateGroupId(transCode);
      //构建标准加密配置参数(只有标准加密才需要构建)
        JSONObject encryptConfig = buildEncryptConfig(aliClient, aliyunTask);
        //HLS标准加密配置(只有标准加密才需要传递)
        request.setEncryptConfig(encryptConfig.toJSONString());
        return aliClient.getAcsResponse(request);
    }

    private  JSONObject buildEncryptConfig(DefaultAcsClient client, AliyunTask aliyunTask) throws ClientException {
        //点播给用户在KMS(秘钥管理服务)中的Service Key，可在用户秘钥管理服务对应的区域看到描述为vod的service key
        String serviceKey = "5ddf9f15-0a94-4e7f-9882-7c6527cfc3c6";
        //随机生成一个加密的秘钥，返回的response包含明文秘钥以及密文秘钥，
        //视频标准加密只需要传递密文秘钥即可
        GenerateDataKeyResponse response = generateDataKey(client, serviceKey);
        JSONObject encryptConfig = new JSONObject();
        //解密接口地址，该参数需要将每次生成的密文秘钥与接口URL拼接生成，表示每个视频的解密的密文秘钥都不一样
        //至于Ciphertext这个解密接口参数的名称，用户可自行制定，这里只作为参考参数名称
        encryptConfig.put("DecryptKeyUri", "http://edufront.lagou.com/front/course/media/alikey?" + "code=" + response.getCiphertextBlob() + "&vid=" + aliyunTask.getFileId());
//        encryptConfig.put("DecryptKeyUri", "http://localhost:8080/alikey?" + "code=" + response.getCiphertextBlob() + "&vid=" + vid);
        //秘钥服务的类型，目前只支持KMS
        encryptConfig.put("KeyServiceType", "KMS");
        //密文秘钥
        encryptConfig.put("CipherText", response.getCiphertextBlob());
        System.out.println("code:" + response.getCiphertextBlob());
        aliyunTask.setFileEdk(response.getCiphertextBlob());
        System.out.println("decode:" + response.getPlaintext());
        aliyunTask.setFileDk(response.getPlaintext());
        return encryptConfig;
    }

    public  GenerateDataKeyResponse generateDataKey(DefaultAcsClient client, String serviceKey) throws ClientException {
        GenerateDataKeyRequest request = new GenerateDataKeyRequest();
        request.setKeyId(serviceKey);
        request.setKeySpec("AES_128");
        return client.getAcsResponse(request);
    }

    /**
     * 等待转码结束
     *
     * @param vid
     * @param taskId
     * @throws ClientException
     */
    public  void waitTranscodeFinish(String vid, String taskId) throws ClientException {
        log.info("等待转码 vid:{} taskId:{}",vid,taskId);
        GetTranscodeSummaryResponse response;
        long startTime = System.currentTimeMillis();
        final String key = "LG_SHOP_TOMCAT:ALIYUN_UPLOAD_" + taskId;
        //处理进度初始化
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(key);
        boundValueOperations.set(String.valueOf(0.00));
        while (true) {
            try {
                response = getTranscodeSummary(vid);
                int totalTaskCount = 0;
                int totalProcess = 0;
                int finishedCount = 0;

                for (GetTranscodeSummaryResponse.TranscodeSummary summary : response.getTranscodeSummaryList()) {
                    //转码状态
                    for (GetTranscodeSummaryResponse.TranscodeSummary.TranscodeJobInfoSummary jobInfoSummary : summary.getTranscodeJobInfoSummaryList()) {
                        totalTaskCount++;
                        totalProcess += jobInfoSummary.getTranscodeProgress();
                        if (jobInfoSummary.getCompleteTime() != null) {
                            finishedCount++;
                        }
                    }
                }
                if(Objects.equals(finishedCount,0)){
                    continue;
                }
                final double percent = ((double)totalProcess/(totalTaskCount * 100)) * 100;
                log.info("转化 totalProcess：{} ，totalTaskCount:{} finishedCount:{}",totalProcess,totalTaskCount,finishedCount);
                boundValueOperations.set(String.valueOf(percent));
                log.info("\r转码总体进度(" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "):" + percent + "%" + ", 已完成任务数:" + finishedCount);
                if (totalTaskCount <= finishedCount) {
                    boundValueOperations.expire(600, TimeUnit.SECONDS);
                    log.info("转码任务执行完成, 总计耗时" + (System.currentTimeMillis() - startTime) + "ms");
                    break;
                }
            } catch (Exception e) {
                log.error("ErrorMessage = " + e.getLocalizedMessage());
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
    }

    private  GetTranscodeSummaryResponse getTranscodeSummary(String vid) throws ClientException {
        GetTranscodeSummaryRequest request = new GetTranscodeSummaryRequest();
        request.setVideoIds(vid);
        return aliClient.getAcsResponse(request);
    }

    /**
     * 获取音频播放地址
     *
     * @param vid
     * @return
     * @throws ClientException
     */
    public  String getPlayUrl(String vid) throws ClientException {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(vid);
        GetPlayInfoResponse acsResponse = aliClient.getAcsResponse(request);
        for (GetPlayInfoResponse.PlayInfo playInfo : acsResponse.getPlayInfoList()) {
            return playInfo.getPlayURL();
        }
        return null;
    }

    /**
     * 获取媒体信息
     *
     * @param vid
     * @return
     * @throws ClientException
     */
    public  GetVideoInfoResponse getMeidaInfo(String vid) throws ClientException {
        GetVideoInfoRequest request = new GetVideoInfoRequest();

        request.setVideoId(vid);

        return aliClient.getAcsResponse(request);
    }


}
