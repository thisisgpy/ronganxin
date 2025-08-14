package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.service.OrgService;
import com.ganpengyu.ronganxin.web.dto.org.CreateOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.SysOrgDto;
import com.ganpengyu.ronganxin.web.dto.org.UpdateOrgDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/1
 */
@Tag(name = "组织接口")
@RestController
@RequestMapping(value = "/api/v1/org")
public class OrgController {

    @Resource
    private OrgService orgService;

    @Operation(summary = "创建组织", parameters = {
            @Parameter(name = "createOrgDto", schema = @Schema(implementation = CreateOrgDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/create")
    public RaxResult<Boolean> createOrg(@Valid @RequestBody CreateOrgDto createOrgDto) {
        orgService.createOrg(createOrgDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新组织", parameters = {
            @Parameter(name = "updateOrgDto", schema = @Schema(implementation = UpdateOrgDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/update")
    public RaxResult<Boolean> updateOrg(@Valid @RequestBody UpdateOrgDto updateOrgDto) {
        orgService.updateOrg(updateOrgDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除组织", parameters = {
            @Parameter(name = "id", description = "组织ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @GetMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeOrg(@PathVariable("id") Long id) {
        orgService.removeOrg(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "通过ID查询组织", parameters = {
            @Parameter(name = "id", description = "组织ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = SysOrgDto.class)
                    })))
    })
    @GetMapping(value = "/get/{id}")
    public RaxResult<SysOrgDto> findOrgDtoById(@PathVariable("id") Long id) {
        SysOrgDto orgDto = orgService.findOrgDtoById(id);
        return RaxResult.ok(orgDto);
    }

    @Operation(summary = "获取组织树", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = SysOrgDto.class)
                    })))
    })
    @GetMapping(value = "/tree")
    public RaxResult<SysOrgDto> findOrgTree() {
        SysOrgDto orgDto = orgService.findOrgTree();
        return RaxResult.ok(orgDto);
    }
}
