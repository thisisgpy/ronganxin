package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.service.RoleService;
import com.ganpengyu.ronganxin.web.dto.role.CreateRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.QueryRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.SysRoleDto;
import com.ganpengyu.ronganxin.web.dto.role.UpdateRoleDto;
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

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Tag(name = "角色接口")
@RestController
@RequestMapping(value = "/api/v1/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "创建角色",
            parameters = {
                    @Parameter(name = "createRoleDto", schema = @Schema(implementation = CreateRoleDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            })
                    ))
            }
    )
    @PostMapping(value = "/create")
    public RaxResult<Boolean> createRole(CreateRoleDto createRoleDto) {
        roleService.createRole(createRoleDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新角色",
            parameters = {
                    @Parameter(name = "updateRoleDto", schema = @Schema(implementation = UpdateRoleDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            })
                    ))
            }
    )
    @PostMapping(value = "/update")
    public RaxResult<Boolean> updateRole(UpdateRoleDto updateRoleDto) {
        roleService.updateRole(updateRoleDto);
        return RaxResult.ok(true);
    }

    @Operation(
            summary = "删除角色",
            parameters = {
                    @Parameter(name = "id", description = "角色ID", required = true)
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            })
                    ))
            }
    )
    @GetMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeRole(@PathVariable("id") Long id) {
        roleService.removeRole(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "通过ID查询角色",
            parameters = {
                    @Parameter(name = "id", description = "角色ID", required = true)
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = SysRoleDto.class)
                            })
                    ))
            }
    )
    @GetMapping(value = "/get/{id}")
    public RaxResult<SysRoleDto> findRoleDtoById(@PathVariable("id") Long id) {
        SysRoleDto sysRoleDto = roleService.findSysRoleDtoById(id);
        return RaxResult.ok(sysRoleDto);
    }

    @Operation(summary = "获取角色列表",
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = List.class)
                            })
                    ))
            }
    )
    @GetMapping(value = "/list")
    public RaxResult<List<SysRoleDto>> findRoleDtoList() {
        List<SysRoleDto> sysRoleDtoList = roleService.findRoleDtoList();
        return RaxResult.ok(sysRoleDtoList);
    }

    @Operation(summary = "分页查询角色",
            parameters = {
                    @Parameter(name = "queryRoleDto", schema = @Schema(implementation = QueryRoleDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = PageResult.class)
                            })
                    ))
            }
    )
    @PostMapping(value = "/page")
    public RaxResult<PageResult<SysRoleDto>> findRoleDtoPage(@Valid @RequestBody QueryRoleDto queryRoleDto) {
        PageResult<SysRoleDto> pageResult = roleService.findRoleDtoPage(queryRoleDto);
        return RaxResult.ok(pageResult);
    }

}
