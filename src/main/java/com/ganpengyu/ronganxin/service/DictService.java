package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.DictBeanMapper;
import com.ganpengyu.ronganxin.beanmapper.DictItemBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysDictDao;
import com.ganpengyu.ronganxin.dao.SysDictItemDao;
import com.ganpengyu.ronganxin.model.SysDict;
import com.ganpengyu.ronganxin.model.SysDictItem;
import com.ganpengyu.ronganxin.web.dto.dict.*;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Service
public class DictService {

    @Resource
    private SysDictDao sysDictDao;

    @Resource
    private SysDictItemDao sysDictItemDao;

    @Resource
    private DictBeanMapper dictBeanMapper;

    @Resource
    private DictItemBeanMapper dictItemBeanMapper;

    /**
     * 创建字典
     *
     * @param createDictDto 包含字典基本信息的DTO对象
     * @return 创建成功返回true
     * @throws RuntimeException 当字典名称或编码已存在，或插入失败时抛出异常
     */
    @Transactional
    public boolean createDict(CreateDictDto createDictDto) {
        CheckUtils.check(!this.checkDictNameExist(createDictDto.getName(), null), "字典名称已存在");
        CheckUtils.check(!this.checkDictCodeExist(createDictDto.getCode(), null), "字典编码已存在");
        SysDict sysDict = dictBeanMapper.toSysDict(createDictDto);
        sysDict.setCreateBy(UserContext.getUsername());
        sysDict.setCreateTime(LocalDateTime.now());
        int rows = sysDictDao.insertSelective(sysDict);
        CheckUtils.check(rows != 0, "新增字典失败");
        return true;
    }

    /**
     * 更新字典信息
     *
     * @param updateDictDto 包含要更新的字典信息的DTO对象
     * @return 更新成功返回true
     * @throws RuntimeException 当字典不存在、名称或编码已存在，或更新失败时抛出异常
     */
    @Transactional
    public boolean updateDict(UpdateDictDto updateDictDto) {
        SysDict sysDict = this.findSysDictById(updateDictDto.getId());
        CheckUtils.check(sysDict != null, "字典不存在");
        CheckUtils.check(!this.checkDictNameExist(updateDictDto.getName(), sysDict.getId()), "字典名称已存在");
        CheckUtils.check(!this.checkDictCodeExist(updateDictDto.getCode(), sysDict.getId()), "字典编码已存在");

        dictBeanMapper.updateSysDict(updateDictDto, sysDict);
        sysDict.setUpdateBy(UserContext.getUsername());
        sysDict.setUpdateTime(LocalDateTime.now());
        int rows = sysDictDao.update(sysDict);
        CheckUtils.check(rows != 0, "更新字典失败");
        return true;
    }

    /**
     * 删除指定ID的字典
     *
     * @param id 要删除的字典ID
     * @return 删除成功返回true
     * @throws RuntimeException 当字典不存在、字典下还有字典项或删除失败时抛出异常
     */
    @Transactional
    public boolean removeDict(Long id) {
        SysDict sysDict = this.findSysDictById(id);
        CheckUtils.check(sysDict != null, "字典不存在");
        long itemCount = this.countByDictId(id);
        CheckUtils.check(itemCount == 0, "请先删除字典项");
        int rows = sysDictDao.deleteById(id);
        CheckUtils.check(rows != 0, "删除字典失败");
        return true;
    }

    /**
     * 分页查询字典列表
     *
     * @param queryDictDto 查询条件对象，包含分页信息和查询条件
     * @return 分页结果对象，包含字典列表和分页信息
     * @throws RuntimeException 当查询参数为空时抛出异常
     */
    public PageResult<SysDictDto> findDictPage(QueryDictDto queryDictDto) {
        CheckUtils.check(null != queryDictDto, "查询参数不能为空");
        int pageNo = queryDictDto.getPageNo() <= 0 ? 1 : queryDictDto.getPageNo();
        int pageSize = queryDictDto.getPageSize() <= 0 ? 10 : queryDictDto.getPageSize();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_enabled", queryDictDto.isEnabled());
        queryWrapper.orderBy("create_time", false);
        if (StringUtils.hasText(queryDictDto.getName())) {
            queryWrapper.like("name", queryDictDto.getName());
        }
        if (StringUtils.hasText(queryDictDto.getCode())) {
            queryWrapper.like("code", queryDictDto.getCode());
        }
        Page<SysDict> page = sysDictDao.paginate(pageNo, pageSize, queryWrapper);
        if (page == null) {
            page = new Page<>(); // 防止空指针
        }
        List<SysDict> records = page.getRecords();
        List<SysDictDto> rows = dictBeanMapper.toSysDictDtoList(records != null ? records : Collections.emptyList());

        PageResult<SysDictDto> pageResult = new PageResult<>();
        pageResult.setRows(rows);
        pageResult.setPageNo(page.getPageNumber());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalCount(page.getTotalRow());
        pageResult.setTotalPages(page.getTotalPage());

        return pageResult;
    }

    /**
     * 根据ID查询字典DTO对象
     *
     * @param id 字典ID
     * @return 字典DTO对象
     * @throws RuntimeException 当字典不存在时抛出异常
     */
    public SysDictDto findDictDtoById(Long id) {
        SysDict sysDict = this.findSysDictById(id);
        CheckUtils.check(sysDict != null, "字典不存在");
        return dictBeanMapper.toSysDictDto(sysDict);
    }

    /**
     * 根据ID查询字典实体对象
     *
     * @param id 字典ID
     * @return 字典实体对象，不存在时返回null
     */
    public SysDict findSysDictById(Long id) {
        return sysDictDao.selectOneById(id);
    }

    /**
     * 检查字典名称是否存在
     *
     * @param name   字典名称
     * @param dictId 字典ID，用于排除自身
     * @return 存在返回true，否则返回false
     */
    private boolean checkDictNameExist(String name, Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        if (null != dictId) {
            queryWrapper.ne("id", dictId);
        }
        return sysDictDao.selectCountByQuery(queryWrapper) > 0;
    }

    /**
     * 检查字典编码是否存在
     *
     * @param code   字典编码
     * @param dictId 字典ID，用于排除自身
     * @return 存在返回true，否则返回false
     */
    private boolean checkDictCodeExist(String code, Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        if (null != dictId) {
            queryWrapper.ne("id", dictId);
        }
        return sysDictDao.selectCountByQuery(queryWrapper) > 0;
    }

    /* ************************************ DictItem *************************************** */

    /**
     * 创建字典项
     *
     * @param createDictItemDto 包含字典项基本信息的DTO对象
     * @return 创建成功返回true
     * @throws RuntimeException 当字典项标签已存在或插入失败时抛出异常
     */
    @Transactional
    public boolean createDictItem(CreateDictItemDto createDictItemDto) {
        CheckUtils.check(!this.checkDictItemExistByLabel(createDictItemDto.getLabel(), null), "字典项标签已存在");
        SysDictItem sysDictItem = dictItemBeanMapper.toSysDictItem(createDictItemDto);
        sysDictItem.setCreateBy(UserContext.getUsername());
        sysDictItem.setCreateTime(LocalDateTime.now());
        int rows = sysDictItemDao.insertSelective(sysDictItem);
        CheckUtils.check(rows != 0, "新增字典项失败");
        return true;
    }

    /**
     * 更新字典项信息
     *
     * @param updateDictItemDto 包含要更新的字典项信息的DTO对象
     * @return 更新成功返回true
     * @throws RuntimeException 当字典项不存在、标签已存在或更新失败时抛出异常
     */
    @Transactional
    public boolean updateDictItem(UpdateDictItemDto updateDictItemDto) {
        SysDictItem sysDictItem = this.findSysDictItemById(updateDictItemDto.getId());
        CheckUtils.check(sysDictItem != null, "字典项不存在");
        CheckUtils.check(!this.checkDictItemExistByLabel(updateDictItemDto.getLabel(), sysDictItem.getId()), "字典项标签已存在");

        dictItemBeanMapper.updateSysDictItem(updateDictItemDto, sysDictItem);
        sysDictItem.setUpdateBy(UserContext.getUsername());
        sysDictItem.setUpdateTime(LocalDateTime.now());
        int rows = sysDictItemDao.update(sysDictItem);
        CheckUtils.check(rows != 0, "修改字典项失败");
        return true;
    }

    /**
     * 删除指定ID的字典项
     *
     * @param id 要删除的字典项ID
     * @return 删除成功返回true
     * @throws RuntimeException 当字典项不存在、还有子字典项或删除失败时抛出异常
     */
    @Transactional
    public boolean removeDictItem(Long id) {
        SysDictItem sysDictItem = this.findSysDictItemById(id);
        CheckUtils.check(sysDictItem != null, "字典项不存在");
        CheckUtils.check(this.countByParentId(sysDictItem.getId()) == 0, "请先删除子字典项");
        int rows = sysDictItemDao.deleteById(id);
        CheckUtils.check(rows != 0, "删除字典项失败");
        return true;
    }

    /**
     * 获取指定字典下的所有字典项树形结构
     *
     * @param dictId 字典ID
     * @return 字典项树形结构列表，根节点的parentId为0或null
     */
    public List<SysDictItemDto> findDictItemTree(Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("dict_id", dictId);
        queryWrapper.orderBy("sort", true); // 按排序字段升序排列
        List<SysDictItem> dictItems = sysDictItemDao.selectListByQuery(queryWrapper);

        if (dictItems == null || dictItems.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为 DTO 列表
        List<SysDictItemDto> dictItemDtos = dictItemBeanMapper.toSysDictItemDtoList(dictItems);

        // 构建树形结构
        return buildDictItemTree(dictItemDtos);
    }

    /**
     * 根据ID查询字典项DTO对象
     *
     * @param id 字典项ID
     * @return 字典项DTO对象
     * @throws RuntimeException 当字典项不存在时抛出异常
     */
    public SysDictItemDto findDictItemDtoById(Long id) {
        SysDictItem sysDictItem = this.findSysDictItemById(id);
        CheckUtils.check(sysDictItem != null, "字典项不存在");
        return dictItemBeanMapper.toSysDictItemDto(sysDictItem);
    }

    /**
     * 根据ID查询字典项实体对象
     *
     * @param id 字典项ID
     * @return 字典项实体对象，不存在时返回null
     */
    public SysDictItem findSysDictItemById(Long id) {
        return sysDictItemDao.selectOneById(id);
    }

    /**
     * 检查字典项标签是否存在
     *
     * @param label  字典项标签
     * @param dictId 字典项ID，用于排除自身
     * @return 存在返回true，否则返回false
     */
    private boolean checkDictItemExistByLabel(String label, Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("label", label);
        if (null != dictId) {
            queryWrapper.ne("dict_id", dictId);
        }
        return sysDictItemDao.selectCountByQuery(queryWrapper) > 0;
    }

    /**
     * 统计指定字典下的字典项数量
     *
     * @param dictId 字典ID
     * @return 字典项数量
     */
    private long countByDictId(Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("dict_id", dictId);
        return sysDictItemDao.selectCountByQuery(queryWrapper);
    }

    /**
     * 统计指定父字典项下的子字典项数量
     *
     * @param parentId 父字典项ID
     * @return 子字典项数量
     */
    private long countByParentId(Long parentId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", parentId);
        return sysDictItemDao.selectCountByQuery(queryWrapper);
    }

    /**
     * 构建字典项树形结构
     *
     * @param dictItemDtos 字典项DTO列表
     * @return 树形结构的根节点列表
     */
    private List<SysDictItemDto> buildDictItemTree(List<SysDictItemDto> dictItemDtos) {
        // 创建一个 map 用于快速查找节点
        Map<Long, SysDictItemDto> itemMap = new HashMap<>();
        for (SysDictItemDto dto : dictItemDtos) {
            itemMap.put(dto.getId(), dto);
        }

        // 存储根节点列表
        List<SysDictItemDto> rootItems = new ArrayList<>();

        // 遍历所有节点，构建父子关系
        for (SysDictItemDto dto : dictItemDtos) {
            Long parentId = dto.getParentId();

            // 如果是根节点（parentId = 0 或 null），添加到根节点列表
            if (parentId == null || parentId == 0) {
                rootItems.add(dto);
            } else {
                // 如果不是根节点，找到父节点并添加到父节点的子节点列表中
                SysDictItemDto parentDto = itemMap.get(parentId);
                if (parentDto != null) {
                    if (parentDto.getChildren() == null) {
                        parentDto.setChildren(new ArrayList<>());
                    }
                    parentDto.getChildren().add(dto);
                }
            }
        }

        return rootItems;
    }

}
