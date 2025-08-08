package com.ganpengyu.ronganxin.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(name = "UserLoginDto")
public class UserLoginDto {

    @NotEmpty(message = "手机号不能为空")
    @Schema(name = "手机号")
    private String mobile;

    @NotEmpty(message = "密码不能为空")
    @Schema(name = "密码")
    private String password;

}
