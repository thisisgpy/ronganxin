package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.service.BankService;
import com.ganpengyu.ronganxin.web.dto.bank.QueryBankDto;
import com.ganpengyu.ronganxin.web.dto.bank.SysBankDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/14
 */
@Tag(name = "银行接口")
@RestController
@RequestMapping(value = "/api/v1/bank")
public class BankController {

    @Resource
    private BankService bankService;

    @Operation(summary = "分页查询银行", parameters = {
            @Parameter(name = "queryBankDto", schema = @Schema(implementation = QueryBankDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = PageResult.class)
                    }))
            )
    }
    )
    @PostMapping(value = "/page")
    public RaxResult<PageResult<SysBankDto>> findBankPage(@RequestBody QueryBankDto queryBankDto) {
        PageResult<SysBankDto> page = bankService.findBankPage(queryBankDto);
        return RaxResult.ok(page);
    }

}
