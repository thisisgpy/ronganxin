package com.ganpengyu.ronganxin.web.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@Schema(name = "CreateOrgDto")
public class CreateOrgDto {

    @NotEmpty(message = "组织名称不能为空")
    @Schema(name = "组织名称")
    private String name;

    @NotEmpty(message = "组织简称不能为空")
    @Schema(name = "组织简称")
    private String nameAbbr;

    @Length(max = 128, message = "组织描述不能超过200个字符")
    @Schema(name = "组织描述")
    private String comment;

    @Min(value = 0, message = "组织父级ID不能小于0")
    @Schema(name = "组织父级ID")
    private Long parentId;

}
