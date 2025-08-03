package com.ganpengyu.ronganxin.web.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@EqualsAndHashCode
@Schema(name = "SysOrgDto")
public class SysOrgDto {

    /**
     * 组织ID
     */
    @Schema(name = "组织ID")
    private Long id;

    /**
     * 组织编码. 4位一级. 0001,00010001,000100010001,以此类推
     */
    @Schema(name = "组织编码")
    private String code;

    /**
     * 组织名称
     */
    @Schema(name = "组织名称")
    private String name;

    /**
     * 组织名称简称
     */
    @Schema(name = "组织名称简称")
    private String nameAbbr;

    /**
     * 组织备注
     */
    @Schema(name = "组织备注")
    private String comment;

    /**
     * 父级组织ID. 0表示没有父组织
     */
    @Schema(name = "组织父级ID")
    private Long parentId;

    /**
     * 子组织
     */
    @Schema(name = "子组织")
    private List<SysOrgDto> children;

}
