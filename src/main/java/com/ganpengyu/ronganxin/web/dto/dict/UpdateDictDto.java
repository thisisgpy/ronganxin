package com.ganpengyu.ronganxin.web.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Data
@Schema(name = "UpdateDictDto")
public class UpdateDictDto {

    @NotNull
    @Schema(name = "字典ID")
    private Long id;

    @NotEmpty
    @Length(max = 64)
    @Schema(name = "字典编码")
    private String code;

    @NotEmpty
    @Length(max = 64)
    @Schema(name = "字典名称")
    private String name;

    @Length(max = 128)
    @Schema(name = "字典备注")
    private String comment;

    @Schema(name = "字典是否启用")
    private Boolean isEnabled;

}
