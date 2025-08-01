package com.ganpengyu.ronganxin.common.page;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PageInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = getOriginalObject(invocation);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        String sql = getSql(metaObject);
        if (!isQuerySql(sql)) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        Paging paging = getPaging(parameterObject);
        if (null == paging) {
            return invocation.proceed();
        }
        long total = getTotal(invocation, metaObject, boundSql);
        paging.setTotalCount(total);
        return rewriteSql(invocation, metaObject, paging);
    }

    /**
     * 获取未被代理的 StatementHandler
     *
     * @param invocation 代理执行上下文
     * @return 未被代理的 StatementHandler
     */
    private StatementHandler getOriginalObject(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        Object object = null;
        while (metaObject.hasGetter("h")) {
            object = metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(object);
        }
        return object == null ? statementHandler : (StatementHandler) object;
    }

    /**
     * 获取要执行的 SQL
     *
     * @param metaObject StatementHandler 包装器
     * @return 要执行的 SQL
     */
    private String getSql(MetaObject metaObject) {
        return (String) metaObject.getValue("delegate.boundSql.sql");
    }

    /**
     * 判断是否为查询 SQL
     *
     * @param sql 要执行的 SQL
     * @return true 查询SQL false 非查询SQL
     */
    private boolean isQuerySql(String sql) {
        sql = sql.trim().toLowerCase();
        return sql.indexOf("select") == 0;
    }

    /**
     * 从传入 DAO 层方法的参数中找到携带分页参数的 {@link Paging} 对象
     *
     * @param parameterObject 传入 DAO 层方法的参数
     * @return 携带分页参数的 {@link Paging} 对象
     */
    private Paging getPaging(Object parameterObject) {
        if (null == parameterObject) {
            return null;
        }
        if (parameterObject instanceof Map) {
            Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
            Collection<Object> values = parameterMap.values();
            for (Object value : values) {
                if (value instanceof Paging) {
                    return (Paging) value;
                }
            }
        } else if (parameterObject instanceof Paging) {
            return (Paging) parameterObject;
        }
        return null;
    }

    /**
     * 创建数据总量查询 SQL 并执行获取数据总量
     *
     * @param invocation 代理执行上下文
     * @param metaObject StatementHandler 包装器
     * @param boundSql   Mybatis 持有的 SQL 对象
     * @return 数据总量
     */
    private long getTotal(Invocation invocation, MetaObject metaObject, BoundSql boundSql) {
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = getSql(metaObject);
        String countSql = "select count(*) as total from (" + sql + ") $_paging";
        Connection connection = (Connection) invocation.getArgs()[0];
        PreparedStatement preparedStatement = null;
        long total = 0L;
        try {
            preparedStatement = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(configuration,
                    countSql,
                    boundSql.getParameterMappings(),
                    boundSql.getParameterObject());
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
                    boundSql.getParameterObject(),
                    countBoundSql);
            parameterHandler.setParameters(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                total = resultSet.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return total;
    }

    /**
     * 重写查询 SQL，增加分页参数
     *
     * @param invocation 代理执行上下文
     * @param metaObject StatementHandler 包装器
     * @param paging     {@link Paging} 分页模型
     * @return 重写后的 SQL
     * @throws Exception
     */
    private Object rewriteSql(Invocation invocation, MetaObject metaObject, Paging paging) throws Exception {
        String sql = getSql(metaObject);
        String newSql = "select * from (" + sql + ") $_paging_table_limit limit ?, ?";
        metaObject.setValue("delegate.boundSql.sql", newSql);
        PreparedStatement preparedStatement = (PreparedStatement) invocation.proceed();
        int parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
        preparedStatement.setLong(parameterCount - 1, (paging.getPageNo() - 1) * paging.getPageSize());
        preparedStatement.setLong(parameterCount, paging.getPageSize());
        return preparedStatement;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
