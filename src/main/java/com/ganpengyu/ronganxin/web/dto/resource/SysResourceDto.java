package com.ganpengyu.ronganxin.web.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Data
@Schema(name = "SysResourceDto")
public class SysResourceDto {

    @Schema(name = "资源ID")
    private Long id;

    @Schema(name = "资源编码")
    private String code;

    @Schema(name = "资源名称")
    private String name;

    @Schema(name = "资源类型. 0:目录, 1:菜单, 2:按钮")
    private Integer type;

    @Schema(name = "父级资源ID. 0表示没有父级资源")
    private Long parentId;

    @Schema(name = "资源路径")
    private String path;

    @Schema(name = "资源组件")
    private String component;

    @Schema(name = "资源图标")
    private String icon;

    @Schema(name = "资源排序")
    private Integer sort;

    @Schema(name = "是否隐藏")
    private Boolean isHidden;

    @Schema(name = "是否缓存")
    private Boolean isKeepAlive;

    @Schema(name = "是否外部链接")
    private Boolean isExternalLink;

    @Schema(name = "是否删除")
    private Boolean isDeleted;

    @Schema(name = "子资源")
    private List<SysResourceDto> children;

}
