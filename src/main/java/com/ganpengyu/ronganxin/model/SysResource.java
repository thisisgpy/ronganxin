package com.ganpengyu.ronganxin.model;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Pengyu Gan
 * CreateDate 2025/7/31
 */

@Data
@Table(value = "sys_resource")
public class SysResource {

    /**
     * 资源ID
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源类型. 0:目录, 1:菜单, 2:按钮
     */
    private Integer type;

    /**
     * 父级资源ID. 0表示没有父级资源
     */
    private Long parentId;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 资源组件
     */
    private String component;

    /**
     * 资源图标
     */
    private String icon;

    /**
     * 资源排序
     */
    private Integer sort;

    /**
     * 是否隐藏. 0:否, 1:是
     */
    private Boolean isHidden;

    /**
     * 是否缓存. 0:否, 1:是
     */
    private Boolean isKeepAlive;

    /**
     * 是否外部链接. 0:否, 1:是
     */
    private Boolean isExternalLink;

    /**
     * 是否删除. 0:否, 1:是
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 信息更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 信息更新人
     */
    private String updateBy;
}