package com.ganpengyu.ronganxin.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@Schema(name = "PageQueryDto")
public class PageQueryDto {

    @Schema(name = "当前页码")
    @Size(min = 1, message = "当前页码最小为1")
    private Integer pageNo;

    @Schema(name = "每页记录数")
    @Size(min = 1, message = "每页记录数最小为1")
    private Integer pageSize;

}
