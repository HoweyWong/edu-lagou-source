package com.lagou.edu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.user.entity.VerificationCode;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leo
 * @since 2020-06-28
 */
public interface IVerificationCodeService extends IService<VerificationCode> {

    boolean save(String telephone);

    boolean checkCode(String telephone, String code);
}
