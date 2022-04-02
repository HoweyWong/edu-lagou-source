package com.lagou.edu.front.upload.service;

import com.aliyun.oss.OSSClient;
import com.lagou.edu.front.config.AliyunConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.lagou.edu.front.upload.vo.UpLoadResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class OssService {

    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private OSSClient ossClient;
    /**
     * 允许上传的格式
     */
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    public UpLoadResult upload(MultipartFile uploadFile) {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        UpLoadResult uploadResult = new UpLoadResult();
        if (!isLegal) {
            uploadResult.setStatus("error");
            return uploadResult;
        }
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            //上传失败
            uploadResult.setStatus("error");
            return uploadResult;
        }
        uploadResult.setStatus("done");
        uploadResult.setName(this.aliyunConfig.getUrlPrefix() + filePath);
        uploadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return uploadResult;
    }

    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }
}