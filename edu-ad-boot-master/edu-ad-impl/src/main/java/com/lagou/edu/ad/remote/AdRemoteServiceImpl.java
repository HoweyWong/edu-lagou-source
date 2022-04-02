package com.lagou.edu.ad.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lagou.edu.ad.api.AdRemoteService;
import com.lagou.edu.ad.api.dto.PromotionAdDTO;
import com.lagou.edu.ad.api.dto.PromotionSpaceDTO;
import com.lagou.edu.ad.entity.PromotionAd;
import com.lagou.edu.ad.entity.PromotionSpace;
import com.lagou.edu.ad.service.IPromotionAdService;
import com.lagou.edu.ad.service.IPromotionSpaceService;
import com.lagou.edu.common.result.ResponseDTO;

import cn.hutool.core.bean.BeanUtil;

@RestController
@RequestMapping("/ad")
public class AdRemoteServiceImpl implements AdRemoteService {

    @Autowired
    private IPromotionSpaceService promotionSpaceService;

    @Autowired
    private IPromotionAdService promotionAdService;

    // 广告位的新增或者修改
    @RequestMapping(value = "/space/saveOrUpdate", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdate(PromotionSpaceDTO promotionSpaceDTO) {
        PromotionSpace entity = new PromotionSpace();
        BeanUtil.copyProperties(promotionSpaceDTO, entity);
        ResponseDTO responseDTO = null;

        // 保存或者更新
        try {
            entity.setUpdateTime(new Date());
            if (entity.getId() != null) {
                promotionSpaceService.updateById(entity);
            } else {
                entity.setIsDel(0);
                entity.setCreateTime(new Date());
                promotionSpaceService.save(entity);
            }
            responseDTO = ResponseDTO.success();
        } catch (Exception e) {
            responseDTO = ResponseDTO.ofError(e.getMessage());
            throw e;
        }
        return responseDTO;
    }

    // 获取单个广告位
    @RequestMapping("/space/getSpaceById")
    public PromotionSpaceDTO getPromotionSpaceById(Integer id) {
        PromotionSpace prospaceSpace = promotionSpaceService.getById(id);
        PromotionSpaceDTO promotionSpaceDTO = new PromotionSpaceDTO();
        BeanUtil.copyProperties(prospaceSpace, promotionSpaceDTO);
        return promotionSpaceDTO;
    }

    // 获取所有广告位
    @RequestMapping("/space/getAllSpaces")
    public List<PromotionSpaceDTO> getAllSpaces() {
        List<PromotionSpace> spaces = promotionSpaceService.list();
        List<PromotionSpaceDTO> spaceDTOS = new ArrayList<>();

        for (PromotionSpace space : spaces) {
            PromotionSpaceDTO promotionSpaceDTO = new PromotionSpaceDTO();
            BeanUtil.copyProperties(space, promotionSpaceDTO);
            spaceDTOS.add(promotionSpaceDTO);
        }
        return spaceDTOS;
    }

    // 新增或者修改广告信息
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResponseDTO saveOrUpdate(PromotionAdDTO promotionAdDTO) {
        PromotionAd entity = new PromotionAd();
        BeanUtil.copyProperties(promotionAdDTO, entity);
        ResponseDTO responseDTO = null;

        // 保存或者更新
        try {
            entity.setUpdateTime(new Date());
            if (entity.getId() != null) {
                // 修改广告信息
                promotionAdService.updateById(entity);
            } else {
                entity.setCreateTime(new Date()); // 创建时间
                // 新增广告
                promotionAdService.save(entity);
            }
            responseDTO = ResponseDTO.success();
        } catch (Exception e) {
            responseDTO = ResponseDTO.ofError(e.getMessage());
            throw e;
        }
        return responseDTO;
    }

    // 根据ID获取对应的广告
    @RequestMapping("/getAdById")
    public PromotionAdDTO getAdById(Integer id) {

        PromotionAdDTO promotionAdDTO = new PromotionAdDTO();
        PromotionAd promotionAd = promotionAdService.getById(id);
        BeanUtil.copyProperties(promotionAd, promotionAdDTO);
        return promotionAdDTO;
    }

    // 获取所有的广告
    @RequestMapping("/getAdList")
    public List<PromotionAdDTO> getAdList() {
        List<PromotionAdDTO> adDTOList = new ArrayList<>();

        List<PromotionAd> adList = promotionAdService.list();
        for (PromotionAd promotionAd : adList) {
            PromotionAdDTO promotionAdDTO = new PromotionAdDTO();
            BeanUtil.copyProperties(promotionAd, promotionAdDTO);
            adDTOList.add(promotionAdDTO);
        }
        return adDTOList;
    }

    // 获取所有的广告
    @RequestMapping("/updateStatus")
    public ResponseDTO updateStatus(Integer id, Integer status) {

        ResponseDTO responseDTO = null;
        try {
            if (status == 0 || status == 1) {
                PromotionAd promotionAd = new PromotionAd();
                promotionAd.setId(id);
                promotionAd.setStatus(status);
                promotionAdService.updateById(promotionAd);
                responseDTO = ResponseDTO.success();
            }
        } catch (Exception e) {
            responseDTO = ResponseDTO.ofError(e.getMessage());
            throw e;
        }
        return responseDTO;
    }

    @GetMapping("/getAllAds")
    public List<PromotionSpaceDTO> getAllAds(String[] spaceKeys) {
        List<PromotionSpaceDTO> promotionSpaceDTOList = new ArrayList<>();

        for (String spaceKey : spaceKeys) {

            // 根据spaceKey获取PromotionSpace
            PromotionSpace promotionSpace = promotionSpaceService.getBySpaceKey(spaceKey);
            if (promotionSpace == null) {
                continue;
            }
            // 根据PromotionSpaceId获取对应的PromotionAd
            List<PromotionAd> promotionAds = promotionAdService.getByPromotionSpaceId(promotionSpace.getId());

            PromotionSpaceDTO promotionSpaceDTO = new PromotionSpaceDTO();
            List<PromotionAdDTO> promotionAdDTOS = new ArrayList<>(promotionAds.size());

            // 拷贝promoteSpace对象的属性到promoteSpaceDTO
            BeanUtil.copyProperties(promotionSpace, promotionSpaceDTO);

            for (PromotionAd promotionAd : promotionAds) {
                PromotionAdDTO promotionAdDTO = new PromotionAdDTO();
                BeanUtil.copyProperties(promotionAd, promotionAdDTO);
                promotionAdDTOS.add(promotionAdDTO);
            }

            promotionSpaceDTO.setAdDTOList(promotionAdDTOS);
            promotionSpaceDTOList.add(promotionSpaceDTO);
        }

        return promotionSpaceDTOList;
    }
}
