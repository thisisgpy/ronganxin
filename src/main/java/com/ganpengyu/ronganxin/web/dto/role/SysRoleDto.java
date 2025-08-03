package com.ganpengyu.ronganxin.web.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Data
@Schema(name = "SysRoleDto")
public class SysRoleDto {

    /**
     * 角色ID
     */
    @Schema(name = "角色ID")
    private Long id;

    /**
     * 角色编码
     */
    @Schema(name = "角色编码")
    private String code;

    /**
     * 角色名称
     */
    @Schema(name = "角色名称")
    private String name;

    /**
     * 角色备注
     */
    @Schema(name = "角色说明")
    private String comment;

    /**
     * 是否删除. 0:否, 1:是
     */
    @Schema(name = "是否删除")
    private Boolean isDeleted;

}
