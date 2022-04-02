package com.lagou.edu.user.controller;


import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.user.service.IVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author felix
 * @since 2020-06-28
 */
/*@RestController
@RequestMapping("/user/vfcode")*/
public class UserPhoneVerificationCodeController {

    @Autowired
    private IVerificationCodeService verificationCodeService;

    /**
     * 发送验证码
     * @return
     */
    @RequestMapping("sendCode")
    public ResponseDTO sendCode(String telephone){
        boolean res = verificationCodeService.save(telephone);
        if(res == true){
            return ResponseDTO.success();
        }else{
            return ResponseDTO.ofError("");
        }
    }

    /**
     * 判断验证码是否正确
     * @return
     */
    @RequestMapping("checkCode")
    public ResponseDTO checkCode(String telephone,String code){
        ResponseDTO responseDTO = null;
        try {
            boolean checkCode = verificationCodeService.checkCode(telephone, code);
            responseDTO = ResponseDTO.success(checkCode);
        }catch (Exception e){
            e.printStackTrace();
            responseDTO = ResponseDTO.ofError(e.getMessage());
        }
       return responseDTO;
    }

}
