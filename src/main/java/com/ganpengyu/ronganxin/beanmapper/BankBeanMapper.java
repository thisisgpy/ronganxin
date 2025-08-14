package com.ganpengyu.ronganxin.beanmapper;

import com.ganpengyu.ronganxin.model.SysBank;
import com.ganpengyu.ronganxin.web.dto.bank.SysBankDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface BankBeanMapper {

    SysBankDto toSysBankDto(SysBank sysBank);

    List<SysBankDto> toSysBankDtoList(List<SysBank> sysBankList);

}
