package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.component.AuthRequired;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.service.UserService;
import com.ganpengyu.ronganxin.web.dto.user.*;
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
 * 用户接口
 *
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Resource
    private UserService userService;

    @AuthRequired
    @GetMapping(value = "/demo")
    public RaxResult<String> demo() {
        return RaxResult.ok(String.valueOf(UserContext.getUserId()));
    }

    @Operation(summary = "创建用户",
            parameters = {
                    @Parameter(name = "createUserDto", schema = @Schema(implementation = CreateUserDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @PostMapping(value = "/create")
    public RaxResult<Boolean> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新用户",
            parameters = {
                    @Parameter(name = "updateUserDto", schema = @Schema(implementation = UpdateUserDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @PostMapping(value = "/update")
    public RaxResult<Boolean> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "重置用户密码",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true)
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @GetMapping(value = "/resetPassword/{id}")
    public RaxResult<Boolean> resetPassword(@PathVariable("id") Long id) {
        userService.resetPassword(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "修改用户密码",
            parameters = {
                    @Parameter(name = "changePasswordDto", schema = @Schema(implementation = ChangePasswordDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @PostMapping(value = "/changePassword")
    public RaxResult<Boolean> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除用户",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true)
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult", properties = {
                                    @StringToClassMapItem(key = "success", value = Boolean.class),
                                    @StringToClassMapItem(key = "message", value = String.class),
                                    @StringToClassMapItem(key = "data", value = Boolean.class)
                            }))),
            })
    @GetMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeUser(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "分页查询用户",
            parameters = {
                    @Parameter(name = "queryUserDto", schema = @Schema(implementation = QueryUserDto.class))
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult",
                                    properties = {
                                            @StringToClassMapItem(key = "success", value = Boolean.class),
                                            @StringToClassMapItem(key = "message", value = String.class),
                                            @StringToClassMapItem(key = "data", value = PageResult.class)
                                    }))),
            })
    @PostMapping(value = "/page")
    public RaxResult<PageResult<SysUserDto>> findUserPage(@Valid @RequestBody QueryUserDto queryUserDto) {
        PageResult<SysUserDto> pageResult = userService.findUserPage(queryUserDto);
        return RaxResult.ok(pageResult);
    }

    @Operation(summary = "通过ID查询用户",
            parameters = {
                    @Parameter(name = "id", description = "用户ID", required = true)
            },
            responses = {
                    @ApiResponse(content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "RaxResult",
                                    properties = {
                                            @StringToClassMapItem(key = "success", value = Boolean.class),
                                            @StringToClassMapItem(key = "message", value = String.class),
                                            @StringToClassMapItem(key = "data", value = SysUserDto.class)
                                    }))),
            })
    @GetMapping(value = "/get/{id}")
    public RaxResult<SysUserDto> findUserDtoById(@PathVariable("id") Long id) {
        SysUserDto sysUserDto = userService.findUserDtoById(id);
        return RaxResult.ok(sysUserDto);
    }
}
