package com.ganpengyu.ronganxin.web.dto.asset;

import com.ganpengyu.ronganxin.common.page.PageQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Data
@Schema(name = "QueryFixedAssetDto")
public class QueryFixedAssetDto extends PageQueryDto {

    @Schema(name = "固定资产名称")
    private String name;

    @Schema(name = "固定资产编号")
    private String code;

    @Schema(name = "固定资产地址")
    private String address;

    @Schema(name = "所属组织ID")
    private Long orgId;

}
