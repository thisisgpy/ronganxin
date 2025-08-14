package com.ganpengyu.ronganxin.web.dto.asset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Data
@Schema(name = "CreateFixedAssetDto")
public class CreateFixedAssetDto {

    @NotEmpty(message = "固定资产名称不能为空")
    @Length(max = 128, message = "固定资产名称长度不能超过128个字符")
    @Schema(name = "固定资产名称")
    private String name;

    @NotEmpty(message = "固定资产编号不能为空")
    @Length(max = 64, message = "固定资产编号长度不能超过64个字符")
    @Schema(name = "固定资产编号")
    private String code;

    @NotEmpty(message = "固定资产地址不能为空")
    @Length(max = 256, message = "固定资产地址长度不能超过256个字符")
    @Schema(name = "固定资产地址")
    private String address;

    @NotNull(message = "固定资产账面价值不能为空")
    @Min(value = 0, message = "固定资产账面价值不能小于0")
    @Schema(name = "固定资产账面价值")
    private Long bookValue;

    @NotNull(message = "所属组织ID不能为空")
    @Min(value = 0, message = "所属组织ID不能小于0")
    @Schema(name = "所属组织ID")
    private Long orgId;

}
