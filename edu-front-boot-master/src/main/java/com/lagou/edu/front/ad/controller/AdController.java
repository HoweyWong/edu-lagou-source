package com.lagou.edu.front.ad.controller;

import com.lagou.edu.ad.api.AdRemoteService;
import com.lagou.edu.ad.api.dto.PromotionAdDTO;
import com.lagou.edu.ad.api.dto.PromotionSpaceDTO;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "广告接口", produces = "application/json")
@RestController
@RequestMapping("/ad")
public class AdController {

    @Autowired
    private AdRemoteService adRemoteService;

    @ApiOperation(value = "获取所有的广告位及其对应的广告")
    @GetMapping("/getAllAds")
    public ResponseDTO<List<PromotionSpaceDTO>> getAllAds(@RequestParam("spaceKeys") String[] spaceKeys){
        List<PromotionSpaceDTO> allAds = adRemoteService.getAllAds(spaceKeys);
        if(allAds.size() == 0){
            return ResponseDTO.response(ResultCode.SUCCESS, null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS, allAds);
    }

    @ApiOperation(value = "获取所有的广告列表")
    @GetMapping("/getAdList")
    public ResponseDTO<List<PromotionAdDTO>> getAdList(){
        List<PromotionAdDTO> allAds = adRemoteService.getAdList();
        if(allAds.size() == 0){
            return ResponseDTO.response(ResultCode.SUCCESS, null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS, allAds);
    }


    @ApiOperation(value = "新增或者修改广告信息")
    @PostMapping("/saveOrUpdate")
    public ResponseDTO saveOrUpdateAd(@RequestBody PromotionAdDTO promotionAdDTO){
        return adRemoteService.saveOrUpdate(promotionAdDTO);
    }

    @ApiOperation(value = "根据Id获取广告信息")
    @GetMapping("/getAdById")
    public ResponseDTO<PromotionAdDTO> getAdById(@RequestParam("id") Integer id){
        PromotionAdDTO promotionAdDTO = adRemoteService.getAdById(id);
        if (promotionAdDTO == null){
            return ResponseDTO.response(ResultCode.SUCCESS,null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS,promotionAdDTO);
    }


    @ApiOperation(value = "更新广告的状态")
    @GetMapping("/updateStatus")
    public ResponseDTO updateStatus(
            @ApiParam(name = "id",value = "广告Id") Integer id,
            @ApiParam(name = "status",value = "状态: 0-下架,1-上架") Integer status
    ){
        return adRemoteService.updateStatus(id,status);
    }


    @ApiOperation(value = "获取所有的广告位")
    @GetMapping("/space/getAllSpaces")
    public ResponseDTO<List<PromotionSpaceDTO>> getAllSpaces(){
        List<PromotionSpaceDTO> allSpaces = adRemoteService.getAllSpaces();
        if(allSpaces.size() == 0){
            return ResponseDTO.response(ResultCode.SUCCESS, null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS, allSpaces);
    }


    @ApiOperation(value = "新增或者修改广告位")
    @PostMapping("/space/saveOrUpdate")
    public ResponseDTO saveOrUpdateSpace(@RequestBody PromotionSpaceDTO promotionSpaceDTO){
        return adRemoteService.saveOrUpdate(promotionSpaceDTO);
    }


    @ApiOperation(value = "根据Id获取广告位")
    @GetMapping("/space/getSpaceById")
    public ResponseDTO<PromotionSpaceDTO> getSpaceById(@RequestParam("id") Integer id){
        PromotionSpaceDTO promotionSpaceDTO = adRemoteService.getPromotionSpaceById(id);
        if(promotionSpaceDTO == null){
            return ResponseDTO.response(ResultCode.SUCCESS,null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS,promotionSpaceDTO);
    }

}
