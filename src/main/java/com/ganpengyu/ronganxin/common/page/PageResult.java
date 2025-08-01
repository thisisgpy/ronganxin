package com.ganpengyu.ronganxin.common.page;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果封装
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Data
@NoArgsConstructor
@Schema(name = "PageResult")
public class PageResult<T> {

    /**
     * 请求数据
     */
    @Schema(name = "请求数据")
    private List<T> rows;

    /**
     * 请求页码
     */
    @Schema(name = "请求页码")
    private Long pageNo;

    /**
     * 请求当页数据量
     */
    @Schema(name = "请求当页数据量")
    private Long pageSize;

    /**
     * 数据总量
     */
    @Schema(name = "数据总量")
    private Long totalCount;

    /**
     * 总页数 = 数据总量 / 请求当页数据量
     */
    @Schema(name = "总页数")
    private Long totalPages;

    public PageResult(Page<T> page, List<T> rows) {
        this.rows = rows;
        this.pageNo = page.getPageNumber();
        this.pageSize = page.getPageSize();
        this.totalPages = page.getTotalPage();
        this.totalCount = page.getTotalRow();
    }

}

