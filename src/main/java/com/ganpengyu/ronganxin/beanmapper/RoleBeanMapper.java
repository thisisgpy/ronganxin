package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysRole;
import com.ganpengyu.ronganxin.web.dto.role.CreateRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.SysRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.UpdateRoleDto;
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
public interface RoleBeanMapper {

    SysRole toSysRole(CreateRoleDto createRoleDto);

    SysRoleDto toSysRoleDto(SysRole sysRole);

    void updateSysRole(UpdateRoleDto updateRoleDto, @MappingTarget SysRole sysRole);

    List<SysRoleDto> toSysRoleDtoList(List<SysRole> sysRoleList);

}
