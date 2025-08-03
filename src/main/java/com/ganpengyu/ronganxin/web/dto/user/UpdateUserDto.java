package com.ganpengyu.ronganxin.web.dto.user;

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
@Schema(name = "UpdateUserDto")
public class UpdateUserDto {

    @NotNull(message = "用户ID不能为空")
    @Schema(name = "用户ID")
    private Long id;

    @Schema(name = "用户所属组织 ID")
    private Long orgId;

    @Schema(name = "手机号")
    private String mobile;

    @Schema(name = "用户名")
    private String name;

    @Schema(name = "性别")
    private String gender;

    @Length(min = 18, max = 18, message = "身份证号必须是18位")
    @Schema(name = "身份证号")
    private String idCard;

    @Size(min = 0, max = 1, message = "不可接受的目标状态")
    @Schema(name = "状态")
    private Integer status;

}
