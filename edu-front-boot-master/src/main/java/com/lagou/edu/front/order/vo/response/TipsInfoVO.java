package com.lagou.edu.front.order.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yzy on 2019-08-15 14:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "弹窗相关VO")
public class TipsInfoVO implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "弹窗标题" , dataType = "integer")
    private String titleText;

    @ApiModelProperty(value = "弹窗文案" , dataType = "integer")
    private String descText;

    @ApiModelProperty(value = "按钮文案" , dataType = "integer")
    private String buttonText;

    public static TipsInfoVO getTipsInfo(String title, String desc, String button){
        TipsInfoVO tips = new TipsInfoVO();
        tips.setTitleText(title);
        tips.setDescText(desc);
        tips.setButtonText(button);
        return tips;
    }
}
