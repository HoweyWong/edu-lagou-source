package com.lagou.edu.user.remote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.common.date.DateUtil;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import com.lagou.edu.user.api.param.UserQueryParam;
import com.lagou.edu.user.entity.User;
import com.lagou.edu.user.service.IUserService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRemoteServiceImpl implements UserRemoteService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private IUserService userService;

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = this.userService.getById(userId);
        if (null == user) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserDTO getUserByPhone(String phone) {
        List<User> list = this.userService.lambdaQuery().eq(User::getPhone, phone).orderByDesc(User::getId).list();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(list.get(0), dto);
        return dto;
    }

    @Override
    public boolean isRegister(String phone) {
        UserDTO userByPhone = getUserByPhone(phone);
        return null != userByPhone && !Boolean.TRUE.equals(userByPhone.getIsDel());
    }

    @Override
    public Page<UserDTO> getPagesUsers(Integer currentPage, Integer pageSize) {
        Page<UserDTO> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        // this.userService.page(page);
        return page;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User();
        BeanUtil.copyProperties(userDTO, user);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        // 重新设置用户昵称
        if (StringUtils.isNotBlank(user.getPhone())) {
            String phone = userDTO.getPhone();
            user.setName("用户" + phone.substring(phone.length() - 4));
        }
        this.userService.save(user);
        BeanUtil.copyProperties(user, userDTO);
        log.info("用户[{}]保存成功", user);
        return userDTO;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        if (null == userDTO.getId() || userDTO.getId() <= 0) {
            log.info("用户id为空，无法更新");
            return false;
        }
        User user = new User();
        BeanUtil.copyProperties(userDTO, user, "create_time");
        if (StringUtils.isNotBlank(userDTO.getPassword())) {
            user.setPassword(encoder.encode(userDTO.getPassword()));
        }
        this.userService.updateById(user);
        log.info("用户[{}]更新成功", user);
        return true;
    }

    @Override
    public boolean isUpdatedPassword(Integer userId) {
        User user = this.userService.getById(userId);
        if (null == user) {
            return false;
        }
        boolean matches = encoder.matches(user.getPhone(), user.getPassword());
        log.info("用户[{}]是否有修改过初始密码[{}]", userId, matches);
        return true;
    }

    @Override
    public boolean setPassword(Integer userId, String password, String configPassword) {
        User user = this.userService.getById(userId);
        if (null == user) {
            return false;
        }
        if (!StringUtils.equals(password, configPassword)) {
            return false;
        }
        user.setPassword(encoder.encode(password));
        this.userService.updateById(user);
        log.info("用户[{}]设置密码成功", userId);
        return true;
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String configPassword) {
        User user = this.userService.getById(userId);
        if (null == user) {
            return false;
        }
        if (!StringUtils.equals(newPassword, configPassword)) {
            return false;
        }
        if (!encoder.matches(oldPassword, user.getPassword())) {
            log.info("用户[{}]旧密码错误", userId);
            return false;
        }
        user.setPassword(encoder.encode(newPassword));
        this.userService.updateById(user);
        log.info("用户[{}]更新密码成功", userId);
        return true;
    }

    @Override
    public Page<UserDTO> getUserPages(UserQueryParam userQueryParam) {

        String phone = userQueryParam.getPhone();
        Integer userId = userQueryParam.getUserId();
        Integer currentPage = userQueryParam.getCurrentPage();
        Integer pageSize = userQueryParam.getPageSize();
        Date startCreateTime = userQueryParam.getStartCreateTime();
        Date endCreateTime = userQueryParam.getEndCreateTime();
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 根据课程名称查询
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (null != startCreateTime && null != endCreateTime) {
            queryWrapper.ge("create_time", startCreateTime);
            queryWrapper.le("create_time", endCreateTime);
        }
        if (null != userId && userId > 0) {
            queryWrapper.eq("id", userId);
        }
        // 根据课程状态查询
        int count = userService.count(queryWrapper);
        queryWrapper.orderByDesc("id");
        IPage<User> selectPage = this.userService.getBaseMapper().selectPage(page, queryWrapper);

        List<UserDTO> userDTOList = new ArrayList<>();
        // 获取课程对应的模块的信息
        for (User user : selectPage.getRecords()) {
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(user, userDTO);
            userDTOList.add(userDTO);
        }

        Page<UserDTO> result = new Page<>();
        // 分页查询结果对象属性的拷贝
        BeanUtil.copyProperties(selectPage, result);
        // 设置分页结果对象record属性
        result.setRecords(userDTOList);
        result.setTotal(count);
        return result;
    }

    @Override
    public boolean forbidUser(Integer userId) {
        User user = this.userService.getById(userId);
        if (null == user) {
            return false;
        }
        user.setUpdateTime(DateUtil.getNowDate());
        user.setIsDel(true);
        user.setStatus("DISABLE");
        boolean result = this.userService.updateById(user);
        if (result) {
            // TODO 发送mq消息，让用户登录失效
        }
        return result;
    }
}
