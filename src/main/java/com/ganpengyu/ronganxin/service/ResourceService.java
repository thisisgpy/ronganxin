package com.ganpengyu.ronganxin.service;

import com.ganpengyu.ronganxin.beanmapper.ResourceBeanMapper;
import com.ganpengyu.ronganxin.dao.SysResourceDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 资源服务
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/3
 */
@Service
public class ResourceService {

    @Resource
    private SysResourceDao sysResourceDao;

    @Resource
    private ResourceBeanMapper resourceBeanMapper;

}
