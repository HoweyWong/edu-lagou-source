package com.lagou.edu.ad.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.ad.api.dto.PromotionAdDTO;
import com.lagou.edu.ad.api.dto.PromotionSpaceDTO;
import com.lagou.edu.ad.entity.PromotionAd;
import com.lagou.edu.ad.entity.PromotionSpace;
import com.lagou.edu.ad.service.IPromotionAdService;
import com.lagou.edu.ad.service.IPromotionSpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*@Slf4j
@RestController
@RequestMapping("/ad")*/
public class AdController {

    @Autowired
    private IPromotionSpaceService promotionSpaceService;

    @Autowired
    private IPromotionAdService promotionAdService;

    /**
     * 查询指定广告定位信息
     * @return
     */
    @GetMapping("/getOneSpace")
    @ResponseBody
    public PromotionSpace getOneSpace(@RequestParam("id") String id) {
        PromotionSpace space = promotionSpaceService.getById(id);
        return space;
    }

    /**
     * 查询所有广告定位信息列表
     * @return
     */
    @GetMapping("/getAllSpace")
    @ResponseBody
    public String getAllSpace() {
        List<PromotionSpace> promotionSpaces = promotionSpaceService.list();
        return promotionSpaces.toString();
    }

    /**
     * 通过SpaceKey列表查询所有广告信息列表
     * @return
     */
    @GetMapping("/getAllAd")
    @ResponseBody
    public String getAllPromotionAdsBySpaceKey(@RequestParam("ids") String ids) {
        List<PromotionAd> promotionAds = new ArrayList<>();
        String [] idsArray = ids.split(",");
        for (int i = 0; i < idsArray.length; i++) {
            PromotionAd promotionAd = promotionAdService.getById(Integer.parseInt(idsArray[i]));
            promotionAds.add(promotionAd);
        }
        return promotionAds.toString();
    }


    /**
     * 通过SpaceKey列表获取广告位以及对应的广告信息
     * @return
     */
    @GetMapping("/getAllAds")
    @ResponseBody
    public List<PromotionSpaceDTO> getAllPromotionAds(@RequestParam("spaceKeys") String[] spaceKeys) {

        List<PromotionSpaceDTO> promotionSpaceDTOList = new ArrayList<>();

        for (String spaceKey:spaceKeys){

            //根据spaceKey获取PromotionSpace
            PromotionSpace promotionSpace = promotionSpaceService.getBySpaceKey(spaceKey);
            //根据PromotionSpaceId获取对应的PromotionAd
            List<PromotionAd> promotionAds = promotionAdService.getByPromotionSpaceId(promotionSpace.getId());


            PromotionSpaceDTO promotionSpaceDTO = new PromotionSpaceDTO();
            List<PromotionAdDTO> promotionAdDTOS = new ArrayList<>(promotionAds.size());

            //拷贝promoteSpace对象的属性到promoteSpaceDTO
            BeanUtil.copyProperties(promotionSpace,promotionSpaceDTO);

            for (PromotionAd promotionAd : promotionAds) {
                PromotionAdDTO promotionAdDTO = new PromotionAdDTO();
                BeanUtil.copyProperties(promotionAd,promotionAdDTO);
                promotionAdDTOS.add(promotionAdDTO);
            }

            promotionSpaceDTO.setAdDTOList(promotionAdDTOS);
            promotionSpaceDTOList.add(promotionSpaceDTO);
        }

        return promotionSpaceDTOList;
    }


}
