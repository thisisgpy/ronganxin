package com.ganpengyu.ronganxin.web.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Data
@Schema(name = "SysDictItemDto")
public class SysDictItemDto {

    @Schema(name = "字典项ID")
    private Long id;

    @Schema(name = "字典ID")
    private Long dictId;

    @Schema(name = "字典编码")
    private String dictCode;

    @Schema(name = "字典项标签")
    private String label;

    @Schema(name = "字典项值")
    private String value;

    @Schema(name = "字典项备注")
    private String comment;

    @Schema(name = "字典项排序")
    private Integer sort;

    @Schema(name = "父级字典项ID")
    private Long parentId;

    @Schema(name = "是否启用")
    private Boolean isEnabled;

    @Schema(name = "子级字典项")
    private List<SysDictItemDto> children;

}
