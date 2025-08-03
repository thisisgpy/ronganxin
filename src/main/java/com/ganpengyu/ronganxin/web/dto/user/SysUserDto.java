package com.ganpengyu.ronganxin.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@Schema(title = "SysUserDto")
public class SysUserDto {

    /**
     * 用户ID
     */
    @Schema(name = "用户 ID")
    private Long id;

    /**
     * 组织ID
     */
    @Schema(name = "用户所属组织 ID")
    private Long orgId;

    /**
     * 手机号
     */
    @Schema(name = "手机号")
    private String mobile;

    /**
     * 用户名
     */
    @Schema(name = "用户名")
    private String name;

    /**
     * 性别. 男, 女
     */
    @Schema(name = "性别")
    private String gender;

    /**
     * 身份证号
     */
    @Schema(name = "身份证号")
    private String idCard;

    /**
     * 是否初始化密码. 0:否, 1:是
     */
    @Schema(name = "是否初始化密码")
    private Boolean isDefaultPassword;

    /**
     * 状态. 0:禁用, 1:启用
     */
    @Schema(name = "状态")
    private Integer status;

    /**
     * 是否删除. 0:否, 1:是
     */
    @Schema(name = "是否删除")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Schema(name = "创建人")
    private String createBy;

    /**
     * 信息更新时间
     */
    @Schema(name = "信息更新时间")
    private LocalDateTime updateTime;

    /**
     * 信息更新人
     */
    @Schema(name = "信息更新人")
    private String updateBy;

}
