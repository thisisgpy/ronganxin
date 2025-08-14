package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.AssetFixed;
import com.ganpengyu.ronganxin.web.dto.asset.CreateFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.FixedAssetDto;
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

    AssetFixed toAssetFixed(CreateFixedAssetDto createFixedAssetDto);

    FixedAssetDto toFixedAssetDto(AssetFixed assetFixed);

    void updateAssetFixed(CreateFixedAssetDto createFixedAssetDto, @MappingTarget AssetFixed assetFixed);

    List<FixedAssetDto> toFixedAssetDtoList(List<AssetFixed> assetFixedList);

}
