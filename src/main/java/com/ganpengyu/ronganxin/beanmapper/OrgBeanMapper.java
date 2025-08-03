package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysOrg;
import com.ganpengyu.ronganxin.web.dto.org.CreateOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.SysOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.UpdateOrgDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrgBeanMapper {

    SysOrg toSysOrg(CreateOrgDto createOrgDto);

    SysOrgDto toSysOrgDto(SysOrg sysOrg);

    void updateSysOrg(UpdateOrgDto updateOrgDto, @MappingTarget SysOrg sysOrg);

    List<SysOrgDto> toSysOrgDtoList(List<SysOrg> sysOrgList);

}
