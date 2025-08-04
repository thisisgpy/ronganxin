package com.ganpengyu.ronganxin.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/4
 */
@Data
@Schema(name = "AssignUserRoleDto")
public class AssignUserRoleDto {

    @NotNull(message = "用户ID不能为空")
    @Schema(name = "用户ID")
    private Long userId;

    @NotNull(message = "角色ID不能为空")
    @Schema(name = "角色ID")
    private List<Long> roleIds;

}
