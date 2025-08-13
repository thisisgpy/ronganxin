package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.service.AuthService;
import com.ganpengyu.ronganxin.web.dto.auth.AssignRoleResourceDto;
import com.ganpengyu.ronganxin.web.dto.auth.AssignUserRoleDto;
import com.ganpengyu.ronganxin.web.dto.auth.UserLoginDto;
import com.ganpengyu.ronganxin.web.dto.user.LoginUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@Tag(name = "认证接口")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "用户登录", parameters = {
            @Parameter(name = "userLoginDto", schema = @Schema(implementation = UserLoginDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaxResult.class)))
    })
    @PostMapping("/login")
    public RaxResult<LoginUserDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return RaxResult.ok(authService.login(userLoginDto));
    }

    @Operation(summary = "用户退出登录", parameters = {
            @Parameter(name = "userId", schema = @Schema(implementation = Long.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaxResult.class)))
    })
    @GetMapping("/logout/{userId}")
    public RaxResult<Boolean> logout(@PathVariable("userId") Long userId) {
        boolean success = authService.logout(userId);
        return RaxResult.ok(success);
    }

    @Operation(summary = "分配用户角色", parameters = {
            @Parameter(name = "assignUserRoleDto", schema = @Schema(implementation = AssignUserRoleDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaxResult.class)))
    })
    @PostMapping("/assignUserRole")
    public RaxResult<Boolean> assignUserRole(@RequestBody @Valid AssignUserRoleDto assignUserRoleDto) {
        authService.assignUserRole(assignUserRoleDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "分配角色资源", parameters = {
            @Parameter(name = "assignRoleResourceDto", schema = @Schema(implementation = AssignRoleResourceDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaxResult.class)))
    })
    @PostMapping("/assignRoleResource")
    public RaxResult<Boolean> assignRoleResource(@RequestBody @Valid AssignRoleResourceDto assignRoleResourceDto) {
        authService.assignRoleResource(assignRoleResourceDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "检查用户是否有资源权限", parameters = {
            @Parameter(name = "userId", schema = @Schema(implementation = Long.class)),
            @Parameter(name = "resourceCode", schema = @Schema(implementation = String.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaxResult.class)))
    })
    @GetMapping("/hasPermission/{userId}/{resourceCode}")
    public RaxResult<Boolean> hasPermission(@PathVariable("userId") Long userId, @PathVariable("resourceCode") String resourceCode) {
        boolean hasPermission = authService.hasPermission(userId, resourceCode);
        return RaxResult.ok(hasPermission);
    }


}
