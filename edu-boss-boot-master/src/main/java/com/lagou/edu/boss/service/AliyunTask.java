package com.lagou.edu.boss.service;

/**
 * @author Bobbao
 * @description
 * @date 2019-12-02 19:37
 */
public class AliyunTask {


    /**
     * 任务id
     * 开悟课程：课时id
     * 开悟大课：视频使用类型+ref_id
     */
    private String taskId;
    private String coverImageUrl;
    private String fileName;
    private Double fileSize;
    private String duration;
    private String fileId;
    private String fileUrl;
    private String fileEdk;
    private String fileDk;
    private Integer durationNum;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileEdk() {
        return fileEdk;
    }

    public void setFileEdk(String fileEdk) {
        this.fileEdk = fileEdk;
    }

    public String getFileDk() {
        return fileDk;
    }

    public void setFileDk(String fileDk) {
        this.fileDk = fileDk;
    }

    public Integer getDurationNum() {
        return durationNum;
    }

    public void setDurationNum(Integer durationNum) {
        this.durationNum = durationNum;
    }
}
