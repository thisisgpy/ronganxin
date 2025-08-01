package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysUser;
import com.ganpengyu.ronganxin.web.dto.CreateUserDto;
import com.ganpengyu.ronganxin.web.dto.SysUserDto;
import com.ganpengyu.ronganxin.web.dto.UpdateUserDto;
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
public interface UserBeanMapper {

    SysUser toSysUser(CreateUserDto createUserDto);

    SysUserDto toSysUserDto(SysUser sysUser);

    void updateSysUser(UpdateUserDto updateUserDto, @MappingTarget SysUser sysUser);

    List<SysUserDto> toSysUserDtoList(List<SysUser> sysUserList);
}
