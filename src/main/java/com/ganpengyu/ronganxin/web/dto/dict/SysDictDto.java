package com.ganpengyu.ronganxin.web.dto.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Data
@Schema(name = "SysDictDto")
public class SysDictDto {

    @Schema(name = "字典ID")
    private Long id;

    @Schema(name = "字典编码")
    private String code;

    @Schema(name = "字典名称")
    private String name;

    @Schema(name = "字典备注")
    private String comment;

    @Schema(name = "字典是否启用")
    private Boolean isEnabled;

}
