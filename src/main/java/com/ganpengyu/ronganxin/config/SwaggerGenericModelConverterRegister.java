package com.ganpengyu.ronganxin.config;

import io.swagger.v3.core.converter.ModelConverters;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Component
public class SwaggerGenericModelConverterRegister {

    private final SwaggerGenericModelConverter genericModelConverter;

    public SwaggerGenericModelConverterRegister(SwaggerGenericModelConverter genericModelConverter) {
        this.genericModelConverter = genericModelConverter;
    }

    @PostConstruct
    public void init() {
        ModelConverters.getInstance().addConverter(genericModelConverter);
    }

}
