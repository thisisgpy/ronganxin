package com.ganpengyu.ronganxin.web;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.page.PageResult;
import com.ganpengyu.ronganxin.service.DictService;
import com.ganpengyu.ronganxin.web.dto.dict.*;
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
 * CreateDate 2025/8/14
 */
@Tag(name = "字典接口")
@RestController
@RequestMapping(value = "/api/v1/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @Operation(summary = "创建字典", parameters = {
            @Parameter(name = "createDictDto", schema = @Schema(implementation = CreateDictDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/create")
    public RaxResult<Boolean> createDict(@RequestBody @Valid CreateDictDto createDictDto) {
        dictService.createDict(createDictDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "编辑字典", parameters = {
            @Parameter(name = "updateDictDto", schema = @Schema(implementation = UpdateDictDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/update")
    public RaxResult<Boolean> updateDict(@RequestBody @Valid UpdateDictDto updateDictDto) {
        dictService.updateDict(updateDictDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除字典", parameters = {
            @Parameter(name = "id", description = "字典ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @GetMapping(value = "/remove/{id}")
    public RaxResult<Boolean> removeDict(@PathVariable("id") Long id) {
        dictService.removeDict(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "根据ID获取字典项", parameters = {
            @Parameter(name = "id", description = "字典项ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = SysDictDto.class)
                    })))
    })
    @GetMapping(value = "/get/{id}")
    public RaxResult<SysDictDto> findDictDtoById(@PathVariable("id") Long id) {
        SysDictDto dict = dictService.findDictDtoById(id);
        return RaxResult.ok(dict);
    }

    @Operation(summary = "分页查询字典", parameters = {
            @Parameter(name = "queryDictDto", schema = @Schema(implementation = QueryDictDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = PageResult.class)
                    })))
    })
    @PostMapping(value = "/page")
    public RaxResult<PageResult<SysDictDto>> findDictPage(@RequestBody @Valid QueryDictDto queryDictDto) {
        PageResult<SysDictDto> pageResult = dictService.findDictPage(queryDictDto);
        return RaxResult.ok(pageResult);
    }

    /* ************************************ DictItem *************************************** */

    @Operation(summary = "创建字典项", parameters = {
            @Parameter(name = "createDictItemDto", schema = @Schema(implementation = CreateDictItemDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/item/create")
    public RaxResult<Boolean> createDictItem(@RequestBody @Valid CreateDictItemDto createDictItemDto) {
        dictService.createDictItem(createDictItemDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "更新字典项", parameters = {
            @Parameter(name = "updateDictItemDto", schema = @Schema(implementation = UpdateDictItemDto.class))
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @PostMapping(value = "/item/update")
    public RaxResult<Boolean> updateDictItem(@Valid @RequestBody UpdateDictItemDto updateDictItemDto) {
        dictService.updateDictItem(updateDictItemDto);
        return RaxResult.ok(true);
    }

    @Operation(summary = "删除字典项", parameters = {
            @Parameter(name = "id", description = "字典项ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = Boolean.class)
                    })))
    })
    @GetMapping(value = "/item/remove/{id}")
    public RaxResult<Boolean> removeDictItem(@PathVariable("id") Long id) {
        dictService.removeDictItem(id);
        return RaxResult.ok(true);
    }

    @Operation(summary = "通过ID查询字典项", parameters = {
            @Parameter(name = "id", description = "字典项ID", required = true)
    }, responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = SysDictItemDto.class)
                    }))
            )
    })
    @GetMapping(value = "/item/get/{id}")
    public RaxResult<SysDictItemDto> getDictItem(@PathVariable("id") Long id) {
        SysDictItemDto dictItem = dictService.findDictItemDtoById(id);
        return RaxResult.ok(dictItem);
    }

    @Operation(summary = "获取字典项树", responses = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "RaxResult", properties = {
                            @StringToClassMapItem(key = "success", value = Boolean.class),
                            @StringToClassMapItem(key = "message", value = String.class),
                            @StringToClassMapItem(key = "data", value = List.class)
                    }))
            )
    })
    @GetMapping(value = "/item/tree/{dictId}")
    public RaxResult<List<SysDictItemDto>> findDictItemTree(@PathVariable("dictId") Long dictId) {
        List<SysDictItemDto> trees = dictService.findDictItemTree(dictId);
        return RaxResult.ok(trees);
    }
}
