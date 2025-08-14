package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.BankBeanMapper;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysBankDao;
import com.ganpengyu.ronganxin.model.SysBank;
import com.ganpengyu.ronganxin.web.dto.bank.QueryBankDto;
import com.ganpengyu.ronganxin.web.dto.bank.SysBankDto;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Service
public class BankService {

    @Resource
    private SysBankDao sysBankDao;

    @Resource
    private BankBeanMapper bankBeanMapper;


    public PageResult<SysBankDto> findBankPage(QueryBankDto queryBankDto) {
        CheckUtils.check(null != queryBankDto, "查询参数不能为空");
        int pageNo = queryBankDto.getPageNo() <= 0 ? 1 : queryBankDto.getPageNo();
        int pageSize = queryBankDto.getPageSize() <= 0 ? 10 : queryBankDto.getPageSize();

        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(queryBankDto.getCode())) {
            queryWrapper.eq("code", queryBankDto.getCode());
        }
        if (StringUtils.hasText(queryBankDto.getName())) {
            queryWrapper.like("name", queryBankDto.getName());
        }
        if (StringUtils.hasText(queryBankDto.getProvince())) {
            queryWrapper.eq("province", queryBankDto.getProvince());
        }
        if (StringUtils.hasText(queryBankDto.getCity())) {
            queryWrapper.eq("city", queryBankDto.getCity());
        }
        if (StringUtils.hasText(queryBankDto.getBranchName())) {
            queryWrapper.like("branch_name", queryBankDto.getBranchName());
        }
        Page<SysBank> page = sysBankDao.paginate(pageNo, pageSize, queryWrapper);
        if (page == null) {
            page = new Page<>(); // 防止空指针
        }
        List<SysBank> records = page.getRecords();
        List<SysBankDto> rows = bankBeanMapper.toSysBankDtoList(records != null ? records : Collections.emptyList());
        PageResult<SysBankDto> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setPageNo(page.getPageNumber());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalCount(page.getTotalRow());
        pageResult.setTotalPages(page.getTotalPage());
        return pageResult;
    }

}
