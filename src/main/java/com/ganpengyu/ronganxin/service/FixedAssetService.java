package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.FixedAssetBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.FixedAssetDao;
import com.ganpengyu.ronganxin.model.FixedAsset;
import com.ganpengyu.ronganxin.model.SysOrg;
import com.ganpengyu.ronganxin.web.dto.asset.CreateFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.FixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.QueryFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.UpdateFixedAssetDto;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/15
 */
@Service
public class FixedAssetService {

    @Resource
    private FixedAssetDao fixedAssetDao;

    @Resource
    private OrgService orgService;

    @Resource
    private FixedAssetBeanMapper fixedAssetBeanMapper;
    @Autowired
    private BeanNameUrlHandlerMapping beanNameHandlerMapping;

    @Transactional
    public boolean createFixedAsset(CreateFixedAssetDto createFixedAssetDto) {
        CheckUtils.check(!checkFixedAssetCodeExist(createFixedAssetDto.getCode(), null), "固定资产编号已存在");
        SysOrg org = orgService.findSysOrgById(createFixedAssetDto.getOrgId());
        CheckUtils.check(null != org, "所属组织不存在");

        FixedAsset fixedAsset = fixedAssetBeanMapper.toFixedAsset(createFixedAssetDto);
        fixedAsset.setCreateBy(UserContext.getUsername());
        fixedAsset.setCreateTime(LocalDateTime.now());

        int rows = fixedAssetDao.insertSelective(fixedAsset);
        CheckUtils.check(rows != 0, "新增固定资产失败");
        return true;
    }

    @Transactional
    public boolean updateFixedAsset(UpdateFixedAssetDto updateFixedAssetDto) {
        FixedAsset fixedAsset = findFixedAssetById(updateFixedAssetDto.getId());
        CheckUtils.check(fixedAsset != null, "固定资产不存在");
        CheckUtils.check(!checkFixedAssetCodeExist(updateFixedAssetDto.getCode(), fixedAsset.getId()), "固定资产编号已存在");

        fixedAssetBeanMapper.updateFixedAsset(updateFixedAssetDto, fixedAsset);
        fixedAsset.setUpdateBy(UserContext.getUsername());
        fixedAsset.setUpdateTime(LocalDateTime.now());
        int rows = fixedAssetDao.update(fixedAsset);
        CheckUtils.check(rows != 0, "更新固定资产失败");
        return true;
    }

    @Transactional
    public boolean removeFixedAsset(Long id) {
        FixedAsset fixedAsset = findFixedAssetById(id);
        CheckUtils.check(fixedAsset != null, "固定资产不存在");
        fixedAsset.setIsDeleted(true);
        fixedAsset.setUpdateBy(UserContext.getUsername());
        fixedAsset.setUpdateTime(LocalDateTime.now());
        int rows = fixedAssetDao.update(fixedAsset);
        CheckUtils.check(rows != 0, "删除固定资产失败");
        return true;
    }

    public PageResult<FixedAssetDto> findFixedAssetPage(QueryFixedAssetDto queryFixedAssetDto) {
        CheckUtils.check(null != queryFixedAssetDto, "查询参数不能为空");
        int pageNo = queryFixedAssetDto.getPageNo() <= 0 ? 1 : queryFixedAssetDto.getPageNo();
        int pageSize = queryFixedAssetDto.getPageSize() <= 0 ? 10 : queryFixedAssetDto.getPageSize();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted", false);
        queryWrapper.orderBy("create_time", false);
        if (StringUtils.hasText(queryFixedAssetDto.getName())) {
            queryWrapper.like("name", queryFixedAssetDto.getName());
        }
        if (StringUtils.hasText(queryFixedAssetDto.getCode())) {
            queryWrapper.eq("code", queryFixedAssetDto.getCode());
        }
        if (StringUtils.hasText(queryFixedAssetDto.getAddress())) {
            queryWrapper.like("address", queryFixedAssetDto.getAddress());
        }
        if (queryFixedAssetDto.getOrgId() != null) {
            queryWrapper.eq("org_id", queryFixedAssetDto.getOrgId());
        }
        Page<FixedAsset> page = fixedAssetDao.paginate(pageNo, pageSize, queryWrapper);
        if (page == null) {
            page = new Page<>(); // 防止空指针
        }
        List<FixedAsset> records = page.getRecords();
        List<FixedAssetDto> rows = fixedAssetBeanMapper.toFixedAssetDtoList(records != null ? records : Collections.emptyList());
        PageResult<FixedAssetDto> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setPageNo(page.getPageNumber());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalCount(page.getTotalRow());
        pageResult.setTotalPages(page.getTotalPage());
        return pageResult;
    }

    public FixedAssetDto findFixedAssetDtoById(Long id) {
        FixedAsset fixedAsset = findFixedAssetById(id);
        CheckUtils.check(fixedAsset != null, "固定资产不存在");
        return fixedAssetBeanMapper.toFixedAssetDto(fixedAsset);
    }

    private FixedAsset findFixedAssetById(Long id) {
        return fixedAssetDao.selectOneById(id);
    }

    private boolean checkFixedAssetCodeExist(String code, Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        queryWrapper.eq("is_deleted", false);
        if (null != id) {
            queryWrapper.ne("id", id);
        }
        return fixedAssetDao.selectCountByQuery(queryWrapper) > 0;
    }

}
