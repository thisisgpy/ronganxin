package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysResource;
import com.ganpengyu.ronganxin.web.dto.resource.CreateResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.SysResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.UpdateResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceBeanMapper {

    SysResource toSysResource(CreateResourceDto createResourceDto);

    SysResourceDto toSysResourceDto(SysResource sysResource);

    void updateSysResource(UpdateResourceDto updateResourceDto, @MappingTarget SysResource sysResource);

    List<SysResourceDto> toSysResourceDtoList(List<SysResource> sysResourceList);

}
