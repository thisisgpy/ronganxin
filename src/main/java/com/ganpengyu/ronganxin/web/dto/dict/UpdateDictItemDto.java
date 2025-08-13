package com.ganpengyu.ronganxin.web.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Data
@Schema(name = "UpdateDictItemDto")
public class UpdateDictItemDto {

    @NotNull(message = "字典项ID不能为空")
    @Schema(name = "字典项ID")
    private Long id;

    @NotEmpty(message = "字典项标签不能为空")
    @Length(max = 64, message = "字典项标签长度不能超过64")
    @Schema(name = "字典项标签")
    private String label;

    @Length(max = 64, message = "字典项值长度不能超过64")
    @Schema(name = "字典项值")
    private String value;

    @Length(max = 128, message = "字典项备注长度不能超过128")
    @Schema(name = "字典项备注")
    private String comment;

    @Min(value = 0, message = "字典项排序不能小于0")
    @Schema(name = "字典项排序")
    private Integer sort;

    @Min(value = 0, message = "父级字典项ID不能小于0")
    @Schema(name = "父级字典项ID")
    private Long parentId;

    @Schema(name = "是否启用")
    private Boolean isEnabled;

}
