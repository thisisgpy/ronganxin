package com.ganpengyu.ronganxin.web.dto.role;

import com.ganpengyu.ronganxin.common.page.PageQueryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Data
@Schema(name = "QueryRoleDto")
public class QueryRoleDto extends PageQueryDto {

    @Length(max = 64, message = "角色名称不能超过64个字符")
    private String name;

}
