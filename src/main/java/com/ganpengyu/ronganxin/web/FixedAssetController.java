package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.service.FixedAssetService;
import com.ganpengyu.ronganxin.web.dto.asset.CreateFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.FixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.QueryFixedAssetDto;
import com.ganpengyu.ronganxin.web.dto.asset.UpdateFixedAssetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/15
 */
@RestController
@RequestMapping(value = "/api/v1/asset/fixed")
public class FixedAssetController {

    @Resource
    private FixedAssetService fixedAssetService;

    @Operation(summary = "创建固定资产", parameters = {
            @Parameter(name = "createFixedAssetDto", schema = @Schema(implementation = CreateFixedAssetDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/create")
    public RaxResult<Boolean> createFixedAsset(@RequestBody @Valid CreateFixedAssetDto createFixedAssetDto) {
        fixedAssetService.createFixedAsset(createFixedAssetDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新固定资产", parameters = {
            @Parameter(name = "updateFixedAssetDto", schema = @Schema(implementation = UpdateFixedAssetDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    }))
            )
    })
    @PostMapping(value = "/update")
    public RaxResult<Boolean> updateFixedAsset(@RequestBody @Valid UpdateFixedAssetDto updateFixedAssetDto) {
        fixedAssetService.updateFixedAsset(updateFixedAssetDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除固定资产", parameters = {
            @Parameter(name = "id", description = "固定资产ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    }))
            )
    })
    @GetMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeFixedAsset(@PathVariable("id") Long id) {
        fixedAssetService.removeFixedAsset(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "通过ID查询固定资产", parameters = {
            @Parameter(name = "id", description = "固定资产ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = FixedAssetDto.class)
                    }))
            )
    })
    @GetMapping(value = "/get/{id}")
    public RaxResult<FixedAssetDto> findFixedAssetDtoById(@PathVariable("id") Long id) {
        FixedAssetDto fixedAssetDto = fixedAssetService.findFixedAssetDtoById(id);
        return RaxResult.ok(fixedAssetDto);
    }

    @Operation(summary = "分页查询固定资产", parameters = {
            @Parameter(name = "queryFixedAssetDto", schema = @Schema(implementation = QueryFixedAssetDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = PageResult.class)
                    }))
            )
    })
    @PostMapping(value = "/page")
    public RaxResult<PageResult<FixedAssetDto>> findFixedAssetPage(@RequestBody @Valid QueryFixedAssetDto queryFixedAssetDto) {
        PageResult<FixedAssetDto> page = fixedAssetService.findFixedAssetPage(queryFixedAssetDto);
        return RaxResult.ok(page);
    }

}
