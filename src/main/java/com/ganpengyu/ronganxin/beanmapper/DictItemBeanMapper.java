package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysDictItem;
import com.ganpengyu.ronganxin.web.dto.dict.CreateDictItemDto;
import com.ganpengyu.ronganxin.web.dto.dict.SysDictItemDto;
import com.ganpengyu.ronganxin.web.dto.dict.UpdateDictItemDto;
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
public interface DictItemBeanMapper {

    SysDictItem toSysDictItem(CreateDictItemDto createDictItemDto);

    SysDictItemDto toSysDictItemDto(SysDictItem sysDictItem);

    void updateSysDictItem(UpdateDictItemDto updateDictItemDto, @MappingTarget SysDictItem sysDictItem);

    List<SysDictItemDto> toSysDictItemDtoList(List<SysDictItem> sysDictItemList);

}
