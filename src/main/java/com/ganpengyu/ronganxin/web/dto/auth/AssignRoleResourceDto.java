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
@Schema(name = "AssignRoleResourceDto")
public class AssignRoleResourceDto {

    @NotNull(message = "角色ID不能为空")
    @Schema(name = "角色ID")
    private Long roleId;

    @NotNull(message = "资源ID不能为空")
    @Schema(name = "资源ID")
    private List<Long> resourceIds;

}
