package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.service.ResourceService;
import com.ganpengyu.ronganxin.web.dto.resource.CreateResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.SysResourceDto;
import com.ganpengyu.ronganxin.web.dto.resource.UpdateResourceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/4
 */
@Tag(name = "资源接口")
@RestController
@RequestMapping(value = "/api/v1/resource")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @Operation(summary = "创建资源",
            parameters = {
                    @Parameter(name = "createResourceDto", schema = @Schema(implementation = CreateResourceDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @RequestMapping(value = "/create")
    public RaxResult<Boolean> createResource(CreateResourceDto createResourceDto) {
        resourceService.createResource(createResourceDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新资源",
            parameters = {
                    @Parameter(name = "updateResourceDto", schema = @Schema(implementation = UpdateResourceDto.class))
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
    @RequestMapping(value = "/update")
    public RaxResult<Boolean> updateResource(UpdateResourceDto updateResourceDto) {
        resourceService.updateResource(updateResourceDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除资源",
            parameters = {
                    @Parameter(name = "id", description = "资源ID", required = true)
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
    @RequestMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeResource(@PathVariable("id") Long id) {
        resourceService.removeResource(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "获取资源树",
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = SysResourceDto.class)
                            })
                    ))
            }
    )
    @GetMapping(value = "/get/{id}")
    public RaxResult<SysResourceDto> getResource(@PathVariable("id") Long id) {
        SysResourceDto resourceDto = resourceService.findSysResourceDtoById(id);
        return RaxResult.ok(resourceDto);
    }


    @Operation(summary = "查询子资源",
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
    @GetMapping(value = "/children/{parentId}")
    public RaxResult<List<SysResourceDto>> findChildren(@PathVariable("parentId") Long parentId) {
        List<SysResourceDto> resourceDtos = resourceService.findChildren(parentId);
        return RaxResult.ok(resourceDtos);
    }

    @Operation(summary = "获取资源树",
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
    @GetMapping(value = "/trees")
    public RaxResult<List<SysResourceDto>> findResourceTree() {
        List<SysResourceDto> trees = resourceService.findResourceTree();
        return RaxResult.ok(trees);
    }

    @Operation(summary = "获取角色资源",
            parameters = {
                    @Parameter(name = "roleId", description = "角色ID", required = true)
            },
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
    @GetMapping(value = "/getResource/{roleId}")
    public RaxResult<List<SysResourceDto>> getResourceByRoleId(@PathVariable("roleId") Long roleId) {
        List<SysResourceDto> resourceDtos = resourceService.findResourceByRoleId(roleId);
        return RaxResult.ok(resourceDtos);
    }

    @Operation(summary = "获取用户菜单",
            parameters = {
                    @Parameter(name = "userId", description = "用户ID", required = true)
            },
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
    @GetMapping(value = "/getMenu/{userId}")
    public RaxResult<List<SysResourceDto>> getMenu(@PathVariable("userId") Long userId) {
        List<SysResourceDto> menus = resourceService.findResourceByUserId(userId);
        return RaxResult.ok(menus);
    }

}
