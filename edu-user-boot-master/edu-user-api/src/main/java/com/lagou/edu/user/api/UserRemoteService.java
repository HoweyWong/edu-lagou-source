package com.lagou.edu.user.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.user.api.dto.UserDTO;
import com.lagou.edu.user.api.param.UserQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${remote.feign.edu-user-boot.name:edu-user-boot}", path = "/user")
public interface UserRemoteService {


    @GetMapping("/getUserById")
    UserDTO getUserById(@RequestParam("userId") Integer userId);

    @GetMapping("/getUserByPhone")
    UserDTO getUserByPhone(@RequestParam("phone") String phone);

    @GetMapping("/isRegister")
    boolean isRegister(@RequestParam("phone") String phone);

    @GetMapping("/getPagesCourses")
    Page<UserDTO> getPagesUsers(@RequestParam("currentPage") Integer currentPage,
                                @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/saveUser")
    UserDTO saveUser(@RequestBody UserDTO userDTO);

    @PostMapping("/updateUser")
    boolean updateUser(@RequestBody UserDTO userDTO);

    @GetMapping("/isUpdatedPassword")
    boolean isUpdatedPassword(@RequestParam("userId") Integer userId);

    @PostMapping("/setPassword")
    boolean setPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password, @RequestParam("configPassword") String configPassword);

    @PostMapping("/updatePassword")
    boolean updatePassword(@RequestParam("userId") Integer userId, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("configPassword") String configPassword);

    @PostMapping(value = "/getUserPages")
    Page<UserDTO> getUserPages(@RequestBody UserQueryParam userQueryParam);

    @PostMapping("/forbidUser")
    boolean forbidUser(@RequestParam("userId") Integer userId);
}
