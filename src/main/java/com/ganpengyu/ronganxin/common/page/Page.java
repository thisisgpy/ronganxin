package com.ganpengyu.ronganxin.common.page;

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
public class Page<T> {

    /**
     * 请求数据
     */
    private List<T> rows;

    /**
     * 请求页码
     */
    private Long pageNo;

    /**
     * 请求当页数据量
     */
    private Integer pageSize;

    /**
     * 数据总量
     */
    private Long totalCount;

    /**
     * 总页数 = 数据总量 / 请求当页数据量
     */
    private Long totalPages;

    public Page(Paging paging, List<T> rows) {
        this.rows = rows;
        this.pageNo = paging.getPageNo();
        this.pageSize = paging.getPageSize();
        this.totalCount = paging.getTotalCount();
        this.totalPages = paging.getTotalPages();
    }

}

