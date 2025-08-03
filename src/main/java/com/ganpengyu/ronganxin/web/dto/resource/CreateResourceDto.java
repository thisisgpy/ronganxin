package com.ganpengyu.ronganxin.web.dto.resource;

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
@Schema(name = "CreateResourceDto")
public class CreateResourceDto {

    @NotEmpty(message = "资源编码不能为空")
    @Length(max = 64, message = "资源编码不能超过64个字符")
    @Schema(name = "资源编码")
    private String code;

    @NotEmpty(message = "资源名称不能为空")
    @Length(max = 64, message = "资源名称不能超过64个字符")
    @Schema(name = "资源名称")
    private String name;

    @NotNull(message = "资源类型不能为空")
    @Schema(name = "资源类型. 0:目录, 1:菜单, 2:按钮")
    private Integer type;

    @NotNull(message = "父级资源ID不能为空")
    @Schema(name = "父级资源ID. 0表示没有父级资源")
    private Long parentId;

    @Length(max = 128, message = "资源路径长度不能超过128")
    @Schema(name = "资源路径")
    private String path;

    @Length(max = 128, message = "资源图标长度不能超过128")
    @Schema(name = "资源组件")
    private String component;

    @Length(max = 64, message = "资源图标长度不能超过64")
    @Schema(name = "资源图标")
    private String icon;

    @Schema(name = "资源排序")
    private Integer sort = 0;

    @Schema(name = "是否隐藏")
    private Boolean isHidden = false;

    @Schema(name = "是否缓存")
    private Boolean isKeepAlive = false;

    @Schema(name = "是否外部链接")
    private Boolean isExternalLink = false;

}
