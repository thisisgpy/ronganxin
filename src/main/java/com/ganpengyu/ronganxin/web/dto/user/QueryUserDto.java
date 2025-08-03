package com.ganpengyu.ronganxin.web.dto.user;

import com.ganpengyu.ronganxin.common.page.PageQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@Schema(name = "QueryUserDto")
public class QueryUserDto extends PageQueryDto {

    @Schema(name = "用户所属组织 ID")
    private Long orgId;

    @Schema(name = "手机号")
    private String mobile;

    @Schema(name = "用户名")
    private String name;

    @Schema(name = "状态")
    @Size(min = 0, max = 1, message = "不可接受的目标状态")
    private Integer status;

}
