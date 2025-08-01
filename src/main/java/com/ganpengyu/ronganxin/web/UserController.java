package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Resource
    private UserService userService;

}
