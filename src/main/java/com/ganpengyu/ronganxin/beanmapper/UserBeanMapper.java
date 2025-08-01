package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysUser;
import com.ganpengyu.ronganxin.web.dto.CreateUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserBeanMapper {

    SysUser toSysUser(CreateUserDto createUserDto);

}
