package com.ganpengyu.ronganxin.web.dto.dict;

import com.ganpengyu.ronganxin.common.page.PageQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Data
@Schema(name = "QueryDictDto")
public class QueryDictDto extends PageQueryDto {

    @Schema(name = "字典名称")
    private String name;

    @Schema(name = "字典编码")
    private String code;

    @Schema(name = "是否启用")
    private boolean enabled = true;

}
