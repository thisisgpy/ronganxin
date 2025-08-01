package com.ganpengyu.ronganxin.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Data
@Schema(name = "创建用户")
public class CreateUserDto {

    @NotEmpty
    @Schema(name = "用户所属组织 ID")
    private Long orgId;

    @NotEmpty
    @Schema(name = "手机号")
    private String mobile;

    @NotEmpty
    @Schema(name = "用户名")
    private String name;

    @NotEmpty
    @Schema(name = "性别")
    private String gender;

    @NotEmpty
    @Length(max = 18)
    @Schema(name = "身份证号")
    private String idCard;

}
