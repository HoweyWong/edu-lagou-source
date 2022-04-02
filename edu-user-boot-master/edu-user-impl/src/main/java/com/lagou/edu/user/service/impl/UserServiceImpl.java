package com.lagou.edu.user.service.impl;

import com.lagou.edu.user.entity.User;
import com.lagou.edu.user.mapper.UserMapper;
import com.lagou.edu.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leo
 * @since 2020-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
