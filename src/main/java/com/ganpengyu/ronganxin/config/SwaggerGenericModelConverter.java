package com.ganpengyu.ronganxin.config;

import com.ganpengyu.ronganxin.common.RaxResult;
import com.ganpengyu.ronganxin.common.page.PageResult;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

/**
 * Swagger 泛型展开配置。@ApiResponse 只需要写统一的 RaxResult 即可，内部泛型会被自动展开。
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Component
public class SwaggerGenericModelConverter implements ModelConverter {

    // 可以扩展支持更多包装类
    private static final Set<Class<?>> WRAPPER_CLASSES = Set.of(RaxResult.class, PageResult.class);

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (type.getType() instanceof ParameterizedType pType) {
            Class<?> rawClass = (Class<?>) pType.getRawType();

            if (WRAPPER_CLASSES.contains(rawClass)) {
                String fieldName = rawClass.equals(RaxResult.class) ? "data" : "rows";
                Type genericType = pType.getActualTypeArguments()[0];

                // 先解析原始类型
                Schema<?> schema = context.resolve(new AnnotatedType(rawClass).resolveAsRef(true));

                // 递归解析泛型参数
                Schema<?> genericSchema = context.resolve(new AnnotatedType(genericType).resolveAsRef(true));
                if (schema.getProperties() != null) {
                    schema.getProperties().put(fieldName, genericSchema);
                }
                return schema;
            }
        }

        // 交给下一个解析器
        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        }
        return null;
    }
}
