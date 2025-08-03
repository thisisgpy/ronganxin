package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.OrgBeanMapper;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.CheckUtils;
import com.ganpengyu.ronganxin.dao.SysOrgDao;
import com.ganpengyu.ronganxin.model.SysOrg;
import com.ganpengyu.ronganxin.web.dto.org.CreateOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.SysOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.UpdateOrgDto;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Service
public class OrgService {

    @Resource
    private SysOrgDao sysOrgDao;

    @Resource
    private OrgBeanMapper orgBeanMapper;

    @Transactional
    public boolean createOrg(CreateOrgDto createOrgDto) {
        SysOrg parentOrg = this.findSysOrgById(createOrgDto.getParentId());
        CheckUtils.check(null != parentOrg, "父组织不存在");
        String code = this.generateOrgCode(createOrgDto.getParentId());
        SysOrg sysOrg = orgBeanMapper.toSysOrg(createOrgDto);
        sysOrg.setCode(code);
        sysOrg.setCreateBy(UserContext.getUsername());
        sysOrg.setCreateTime(LocalDateTime.now());
        int rows = sysOrgDao.insertSelective(sysOrg);
        CheckUtils.check(rows != 0, "新增组织失败");
        return true;
    }

    /**
     * 更新组织
     * 1. 使用组织ID查询组织
     * 2. 如果组织不存在，则抛出异常
     * 3. 如果组织存在，判断新旧父组织是否一致
     * 4. 如果新旧父组织一致，则直接更新组织（为空的字段不更新）
     * 5. 如果新旧父组织不一致
     * 5.1 如果新的父组织不是 0，则检查变更后的层级关系是否合规
     * 5.1.1 新的父组织不能是自己
     * 5.1.2 新的父组织不能是自己的子孙组织
     * 6. 根据新的父组织 ID，生成当前组织的新编码并赋值
     * 7. 根据当前组织的新旧编码，重新计算子孙组织编码
     * 8. 使用事务更新组织及其子孙组织
     * 9. 如果全部流程都正常，则返回 true，否则返回 false
     *
     * @param updateOrgDto 更新组织的数据
     * @return 更新是否成功
     */
    @Transactional
    public boolean updateOrg(UpdateOrgDto updateOrgDto) {
        // 1. 查询要更新的组织是否存在
        SysOrg existingOrg = this.findSysOrgById(updateOrgDto.getId());
        CheckUtils.check(null != existingOrg, "组织不存在");

        Long orgId = existingOrg.getId();
        Long oldParentId = existingOrg.getParentId();
        Long newParentId = updateOrgDto.getParentId();

        // 2. 判断新旧父组织是否相同
        boolean isSameParent = oldParentId.equals(newParentId);

        // 3. 准备需要更新的组织列表（包括子孙组织）
        List<SysOrg> preUpdateOrgList = new ArrayList<>();

        String oldCode = existingOrg.getCode();
        String newCode = oldCode;

        // 4. 如果父组织发生变化，处理层级关系和编码变更
        if (!isSameParent) {
            // 4.1 非根组织需要校验父子关系合法性
            if (!newParentId.equals(0L)) {
                SysOrg newParentOrg = this.findSysOrgById(newParentId);
                CheckUtils.check(null != newParentOrg, "父组织不存在");
                CheckUtils.check(!newParentOrg.getId().equals(orgId), "不能将自己设置为父级组织");
                CheckUtils.check(!newParentOrg.getCode().startsWith(oldCode), "不能将子孙组织设置为父级组织");
            }
            // 4.2 生成新的组织编码，并重新计算子孙组织编码
            newCode = this.generateOrgCode(newParentId);
            preUpdateOrgList.addAll(this.reCalculateDescendantOrgCodes(oldCode, newCode));
        }

        // 5. 更新当前组织信息（不更新空字段）
        orgBeanMapper.updateSysOrg(updateOrgDto, existingOrg);
        existingOrg.setUpdateBy(UserContext.getUsername());
        existingOrg.setUpdateTime(LocalDateTime.now());
        if (!isSameParent) {
            existingOrg.setCode(newCode);
        }
        preUpdateOrgList.add(existingOrg);

        // 6. 批量更新组织及其子孙组织信息
        for (SysOrg sysOrg : preUpdateOrgList) {
            int rows = sysOrgDao.update(sysOrg);
            CheckUtils.check(rows != 0, "更新组织失败");
        }
        return true;
    }

    /**
     * 删除组织
     * 1. 检查组织是否存在
     * 2. 检查组织是否存在子组织
     * 3. 如果组织存在子组织，则抛出异常
     * 4. 如果组织不存在子组织，则删除组织
     * 5. 返回删除是否成功
     *
     * @param id 组织ID
     * @return 删除是否成功
     */
    @Transactional
    public boolean removeOrg(Long id) {
        // 查找要删除的组织是否存在
        SysOrg existingOrg = this.findSysOrgById(id);
        CheckUtils.check(existingOrg != null, "组织不存在");

        // 检查组织是否存在子组织
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", id);
        long childrenCount = sysOrgDao.selectCountByQuery(queryWrapper);
        CheckUtils.check(childrenCount == 0, "组织存在子组织，请先删除子组织");

        // 执行删除操作
        int rows = sysOrgDao.deleteById(id);
        CheckUtils.check(rows != 0, "删除组织失败");
        return true;
    }

    /**
     * 查询完整组织树
     * 1. 查询所有的组织
     * 2. 按照 parentId 将组织构造为树形结构
     *
     * @return 组织树
     */
    public SysOrgDto findOrgTree() {
        List<SysOrg> sysOrgs = sysOrgDao.selectAll();
        if (sysOrgs == null || sysOrgs.isEmpty()) {
            return null;
        }

        List<SysOrgDto> sysOrgDtoList = orgBeanMapper.toSysOrgDtoList(sysOrgs);

        // 初始化 children 并构建 id -> node 映射
        Map<Long, SysOrgDto> orgMap = new HashMap<>();
        SysOrgDto root = null;

        for (SysOrgDto dto : sysOrgDtoList) {
            if (dto.getChildren() == null) {
                dto.setChildren(new ArrayList<>());
            }
            orgMap.put(dto.getId(), dto);

            // 查找根节点（parentId == 0）
            if (dto.getParentId() != null && dto.getParentId().equals(0L)) {
                CheckUtils.check(root == null, "存在多个根节点，请检查数据一致性");
                root = dto;
            }
        }

        // 构建树结构
        for (SysOrgDto dto : sysOrgDtoList) {
            Long parentId = dto.getParentId();
            if (parentId != null && !parentId.equals(0L)) {
                SysOrgDto parent = orgMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return root;
    }


    public SysOrg findSysOrgById(Long id) {
        return sysOrgDao.selectOneById(id);
    }

    public SysOrgDto findOrgDtoById(Long id) {
        SysOrg sysOrg = sysOrgDao.selectOneById(id);
        CheckUtils.check(sysOrg != null, "组织不存在");
        return orgBeanMapper.toSysOrgDto(sysOrg);
    }

    /**
     * 生成组织编码
     * 组织编码规则：
     * 1. 组织编码每4位数为一级，每一级从 0001 开始，顺序编号
     * 2. 例如：0001 表示一级组织，00010001 表示二级组织，000100010001 表示三级组织，0001000100010001 表示四级组织
     * 组织编码生成规则：
     * 1. 如果 parentId 为空，则抛出异常
     * 2. 如果 parentId 不为 0, 则检查父组织是否存在，不存在则抛出异常
     * 3. 如果父组织存在，查找已有子组织的最大编码
     * 4. 如果最大编码为空，则直接返回父组织编码 + '0001'
     * 5. 如果最大编码不为空，则将最大编码的最后一位加 1
     * 6. 返回组织编码
     *
     * @param parentId 父级组织 ID
     * @return 组织编码
     */
    private String generateOrgCode(Long parentId) {
        // 校验父组织ID是否为空
        CheckUtils.check(null != parentId, "父组织ID不能为空");

        // 如果 parentId 不为 0，需要校验父组织是否存在
        if (!parentId.equals(0L)) {
            SysOrg parentOrg = this.findSysOrgById(parentId);
            CheckUtils.check(null != parentOrg, "父组织不存在");
        }

        // 查询当前父组织下最大的子组织编码
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", parentId)
                .orderBy("code", false)
                .limit(1);
        SysOrg maxOrg = sysOrgDao.selectOneByQuery(queryWrapper);

        // 如果没有子组织，根据是否为根节点生成初始编码
        if (null == maxOrg) {
            if (parentId.equals(0L)) {
                return "0001";
            } else {
                SysOrg parentOrg = this.findSysOrgById(parentId);
                CheckUtils.check(null != parentOrg, "父组织不存在");
                return parentOrg.getCode() + "0001";
            }
        }

        // 截取当前最大编码的最后四位，并加一生成新的编码
        String lastFourDigits = maxOrg.getCode().substring(maxOrg.getCode().length() - 4);
        int nextCode = Integer.parseInt(lastFourDigits) + 1;

        // 拼接新编码并返回
        return maxOrg.getCode().substring(0, maxOrg.getCode().length() - 4) + String.format("%04d", nextCode);
    }

    /**
     * 重新计算后代组织机构的编码
     *
     * @param oldParentCode 原始父级组织编码
     * @param newParentCode 新的父级组织编码
     * @return 更新后的后代组织机构列表
     */
    private List<SysOrg> reCalculateDescendantOrgCodes(String oldParentCode, String newParentCode) {
        // 查询所有以oldParentCode为前缀的后代组织机构
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.likeLeft("code", oldParentCode);
        List<SysOrg> descendantOrgs = sysOrgDao.selectListByQuery(queryWrapper);

        // 遍历所有后代组织机构，将编码中的旧父级编码替换为新父级编码
        for (SysOrg descendantOrg : descendantOrgs) {
            String oldCode = descendantOrg.getCode();
            String newCode = oldCode.replace(oldParentCode, newParentCode);
            descendantOrg.setCode(newCode);
            descendantOrg.setUpdateBy(UserContext.getUsername());
            descendantOrg.setUpdateTime(LocalDateTime.now());
        }
        return descendantOrgs;
    }

}
