package com.lagou.edu.message.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jinglupeng on 3/23/16.
 */
public class PushUtils {

    private final static String separator = "_";

    private final static String ANONYMOUS_ID = "-1";

    public static String getCompanyIdUserId(String userId, String companyId) {
        return new StringBuilder(companyId).append(separator).append(userId).toString();
    }


    /**
     * 同时有staffId和userId同时在两个房间内
     * @param userId
     * @param staffId
     * @param companyId
     * @return
     */
    public static List<String> getRoomList(String userId, String staffId, String companyId){
        companyId = StringUtils.isBlank(companyId) ? "-1" : companyId;
        userId = StringUtils.isBlank(userId) ? "-1" : userId;
        staffId = StringUtils.isBlank(staffId) ? "-1" : staffId;
        List<String> roomList = new ArrayList<>();
        if(userId.equals(ANONYMOUS_ID)){
            roomList.add(new StringBuilder(companyId).append(separator).append("-1").append(separator).append(staffId).toString());
            roomList.add(new StringBuilder("-1").append(separator).append("-1").append(separator).append(staffId).toString());
        } else if( staffId.equals(ANONYMOUS_ID)){
            roomList.add(new StringBuilder(companyId).append(separator).append(userId).append(separator).append("-1").toString());
            roomList.add(new StringBuilder("-1").append(separator).append(userId).append(separator).append("-1").toString());
        } else {
            roomList.add(new StringBuilder(companyId).append(separator).append(userId).append(separator).append("-1").toString());
            roomList.add(new StringBuilder(companyId).append(separator).append("-1").append(separator).append(staffId).toString());
            roomList.add(new StringBuilder(companyId).append(separator).append(userId).append(separator).append(staffId).toString());
            roomList.add(new StringBuilder("-1").append(separator).append(userId).append(separator).append("-1").toString());
            roomList.add(new StringBuilder("-1").append(separator).append("-1").append(separator).append(staffId).toString());
            roomList.add(new StringBuilder("-1").append(separator).append(userId).append(separator).append(staffId).toString());
        }
        return roomList;
    }


    /**
     * 获取可推送的房间号
     * @param companyId
     * @param userId
     * @param staffId
     * @return
     */
    public static String getRoom(String companyId, Integer userId, String staffId){
        companyId = StringUtils.isBlank(companyId) ? "-1" : companyId;
        userId = null == userId ? -1 : userId;
        staffId = StringUtils.isBlank(staffId) ? "-1" : staffId;
        return new StringBuilder(companyId).append(separator).append(userId).append(separator).append(staffId).toString();
    }

    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        List<String> list= Lists.newArrayList("1","2");
        map.put("room",list);
        List<String> blist= (List<String>) map.get("room");
        System.out.println(blist);
    }
}
