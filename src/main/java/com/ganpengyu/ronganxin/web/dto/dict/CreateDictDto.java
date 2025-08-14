package com.ganpengyu.ronganxin.web.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Data
@Schema(name = "CreateDictDto")
public class CreateDictDto {

    @NotEmpty(message = "字典编码不能为空")
    @Length(max = 64, message = "字典编码长度不能超过64个字符")
    @Schema(name = "字典编码")
    private String code;

    @NotEmpty(message = "字典名称不能为空")
    @Length(max = 64, message = "字典名称长度不能超过64个字符")
    @Schema(name = "字典名称")
    private String name;

    @Length(max = 128, message = "字典备注长度不能超过128个字符")
    @Schema(name = "字典备注")
    private String comment;

    @Schema(name = "字典是否启用")
    private Boolean isEnabled;

}
