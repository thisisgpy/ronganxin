package com.ganpengyu.ronganxin.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Data
@Schema(name = "CreateUserDto")
public class CreateUserDto {

    @NotNull(message = "组织ID不能为空")
    @Schema(name = "用户所属组织 ID")
    private Long orgId;

    @NotEmpty(message = "手机号必填")
    @Schema(name = "手机号")
    private String mobile;

    @NotEmpty(message = "用户名必填")
    @Schema(name = "用户名")
    private String name;

    @NotEmpty(message = "性别必填")
    @Schema(name = "性别")
    private String gender;

    @NotEmpty(message = "身份证号必填")
    @Length(min = 18, max = 18, message = "身份证号必须是18位")
    @Schema(name = "身份证号")
    private String idCard;

}
