package com.ganpengyu.ronganxin.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Data
@Schema(name = "ChangePasswordDto")
public class ChangePasswordDto {

    @NotNull(message = "用户ID不能为空")
    @Schema(name = "用户ID")
    private Long id;

    @NotNull(message = "旧密码不能为空")
    @Schema(name = "旧密码")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    @Schema(name = "新密码")
    private String newPassword;

}
