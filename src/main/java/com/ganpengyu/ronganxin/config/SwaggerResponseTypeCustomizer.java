package com.ganpengyu.ronganxin.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Component
public class SwaggerResponseTypeCustomizer {

    @Bean
    public OperationCustomizer useMethodReturnType() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Type returnType = handlerMethod.getMethod().getGenericReturnType();

            // 替换所有 200 响应的 schema
            for (Map.Entry<String, ApiResponse> entry : operation.getResponses().entrySet()) {
                if (entry.getKey().equals("200") && entry.getValue().getContent() != null) {
                    entry.getValue().getContent().forEach((mediaTypeKey, mediaType) -> {
                        mediaType.setSchema(new io.swagger.v3.oas.models.media.Schema<>().$ref(
                                "#/components/schemas/" + returnType.getTypeName()
                        ));
                    });
                }
            }
            return operation;
        };
    }
}
