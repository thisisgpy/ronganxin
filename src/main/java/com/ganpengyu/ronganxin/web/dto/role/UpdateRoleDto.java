package com.ganpengyu.ronganxin.web.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Data
@Schema(name = "UpdateRoleDto")
public class UpdateRoleDto {

    @NotNull(message = "角色ID不能为空")
    private Long id;

    @NotEmpty(message = "角色编码不能为空")
    @Length(max = 64, message = "角色编码不能超过64个字符")
    private String code;

    @NotEmpty(message = "角色名称不能为空")
    @Length(max = 64, message = "角色名称不能超过64个字符")
    private String name;

    @Length(max = 128, message = "角色说明不能超过64==128个字符")
    private String comment;

}
