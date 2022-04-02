package com.lagou.edu.user.remote;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.user.api.VerificationCodeRemoteService;
import com.lagou.edu.user.exception.ExpireCodeRuntimeException;
import com.lagou.edu.user.exception.IncorrectCodeRuntimteException;
import com.lagou.edu.user.service.IVerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/vfcode")
public class VerificationCodeRemoteServiceImpl implements VerificationCodeRemoteService {

    @Autowired
    private IVerificationCodeService verificationCodeService;

    @RequestMapping(value = "/sendCode")
    public ResponseDTO sendCode(String telephone) {
        ResponseDTO responseDTO = null;
        try {
            boolean res = verificationCodeService.save(telephone);
            responseDTO = ResponseDTO.success("发送成功");
        }catch (Exception e){
            e.printStackTrace();
            responseDTO = ResponseDTO.ofError(e.getMessage());
        }
        return responseDTO;
    }

    @RequestMapping(value = "/checkCode")
    /*
        验证码不正确,设置状态码为203
        验证码过期,设置状态码为204
     */
    public ResponseDTO checkCode(String telephone, String code) {
        ResponseDTO responseDTO = null;
        try {
            boolean checkCode = verificationCodeService.checkCode(telephone, code);
            responseDTO = ResponseDTO.success("验证成功");
        }catch (IncorrectCodeRuntimteException e){
            responseDTO = ResponseDTO.response(203,e.getMessage(),null);
        }catch (ExpireCodeRuntimeException e){
            responseDTO = ResponseDTO.response(204,e.getMessage(),null);
        }catch (Exception e){
            e.printStackTrace();
            responseDTO = ResponseDTO.ofError(e.getMessage());
        }
        return responseDTO;
    }
}
