package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.FixedAsset;
import com.ganpengyu.ronganxin.web.dto.asset.CreateFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.FixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.UpdateFixedAssetDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface FixedAssetBeanMapper {

    FixedAsset toFixedAsset(CreateFixedAssetDto createFixedAssetDto);

    FixedAssetDto toFixedAssetDto(FixedAsset fixedAsset);

    void updateFixedAsset(UpdateFixedAssetDto updateFixedAssetDto, @MappingTarget FixedAsset fixedAsset);

    List<FixedAssetDto> toFixedAssetDtoList(List<FixedAsset> fixedAssetList);

}
