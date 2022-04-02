package com.lagou.edu.boss.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.lagou.edu.boss.service.impl.AliYunVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;


/**
 * @author Bobbao
 * @description
 * @date 2019-08-21 10:45
 */
@Slf4j
@Service
public class AliyunService {

    private final Executor executor = Executors.newSingleThreadExecutor();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AliYunVideoService aliYunVideoService;

    /**
     * 课程转码
     *
     * @throws ClientException
     */
    public void processTranscode(AliyunTask courseMediaDTO, Function<AliyunTask, Void> function) throws ClientException {
        executor.execute(new AliyunTransCodeProcess(courseMediaDTO, function));
    }

    /**
     * 转码处理进度
     *
     * @param taskId
     * @throws ClientException
     */
    public double processTranscodePercent(String taskId){
        final String key =  "LG_SHOP_TOMCAT:ALIYUN_UPLOAD_" + taskId;
        //处理进度初始化
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(key);

        Object percent = boundValueOperations.get();
        log.info("转化率 perscent:{}",percent);
        if (percent == null) {
            return 0;
        } else {
            return Double.valueOf(percent.toString());
        }
    }


    class AliyunTransCodeProcess implements Runnable {

        private final AliyunTask aliyunTask;
        private final Function<AliyunTask, Void> function;

        AliyunTransCodeProcess(AliyunTask aliyunTask, Function<AliyunTask, Void> function) {
            this.aliyunTask = aliyunTask;
            this.function = function;
        }

        @Override
        public void run() {
            try {
                String fileId = aliyunTask.getFileId();

                String transCode = AliYunVideoService.VIDEO_TRANSCODE;
                aliYunVideoService.transcodeVideo(aliyunTask, transCode);

                String taskId =  "aliyun:transCode:"+aliyunTask.getTaskId();

                aliYunVideoService.waitTranscodeFinish(fileId, taskId);

                aliyunTask.setFileUrl(aliYunVideoService.getPlayUrl(fileId));

                final GetVideoInfoResponse.Video video = aliYunVideoService.getMeidaInfo(fileId).getVideo();

                final int totalSeconds = video.getDuration().intValue();
                final int minute = totalSeconds / 60;
                final int seconds = totalSeconds % 60;
                aliyunTask.setDuration(buildTime(minute) + ":" + buildTime(seconds));
                aliyunTask.setDurationNum(totalSeconds);
                aliyunTask.setCoverImageUrl(video.getCoverURL());
                final Long totalBytes = video.getSize();
                final Double totalMbs = Double.valueOf(totalBytes) / 1024 / 1024;
                BigDecimal bg = BigDecimal.valueOf(totalMbs).setScale(2, RoundingMode.UP);

                aliyunTask.setFileSize(Double.valueOf(bg.doubleValue()));

                function.apply(aliyunTask);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
    public static String buildTime(int time) {
        return time == 0 ? "00" : (time < 10 ? "0" + time : String.valueOf(time));
    }

    public static void main(String[] args) {

        final Double totalMbs = Double.valueOf(1213213d) / 1024 / 1024;

        BigDecimal bg = new BigDecimal(totalMbs).setScale(2, RoundingMode.UP);

        System.out.println(bg.doubleValue());

    }
}
