package com.ganpengyu.ronganxin.web.dto.bank;

import com.ganpengyu.ronganxin.common.page.PageQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Data
@Schema(name = "QueryBankDto")
public class QueryBankDto extends PageQueryDto {

    @Schema(name = "联行号")
    private String code;

    @Schema(name = "银行名称")
    private String name;

    @Schema(name = "省份")
    private String province;

    @Schema(name = "城市")
    private String city;

    @Schema(name = "支行名称")
    private String branchName;

}
