package com.ganpengyu.ronganxin.web.dto.user;

import com.ganpengyu.ronganxin.web.dto.resource.SysResourceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/5
 */
@Data
@Schema(name = "登录用户DTO")
public class LoginUserDto {

    @Schema(name = "用户基本信息")
    private SysUserDto userInfo;

    @Schema(name = "token")
    private String token;

    @Schema(name = "路由菜单")
    private List<SysResourceDto> menus;

}
