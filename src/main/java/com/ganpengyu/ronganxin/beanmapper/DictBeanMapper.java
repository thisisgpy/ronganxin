package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysDict;
import com.ganpengyu.ronganxin.web.dto.dict.CreateDictDto;
import com.ganpengyu.ronganxin.web.dto.dict.SysDictDto;
import com.ganpengyu.ronganxin.web.dto.dict.UpdateDictDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DictBeanMapper {

    SysDict toSysDict(CreateDictDto createDictDto);

    SysDictDto toSysDictDto(SysDict sysDict);

    void updateSysDict(UpdateDictDto updateDictDto, @MappingTarget SysDict sysDict);

    List<SysDictDto> toSysDictDtoList(List<SysDict> sysDictList);

}
