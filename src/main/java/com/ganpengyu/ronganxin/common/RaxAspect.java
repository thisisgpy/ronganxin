package com.ganpengyu.ronganxin.common;

import com.ganpengyu.ronganxin.common.context.LoginUser;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * 接口返回切面
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Slf4j
@Aspect
@Component
public class RaxAspect {

    @Pointcut("execution(public com.ganpengyu.ronganxin.common.RaxResult *(..))")
    public void execute() {

    }

    /**
     * 切面逻辑
     *
     * @param pjp {@link ProceedingJoinPoint} 切点上下文
     * @return {@link RaxResult} 请求响应数据模型
     */
    @Around("execute()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        // TODO 权限校验
        LoginUser user = new LoginUser();
        user.setId(1L);
        user.setUsername("admin");
        UserContext.setContext(user);

        RaxResult<?> result;
        StopWatch stopWatch = new StopWatch();
        try {
            result = (RaxResult<?>) pjp.proceed();
            log.info("{} cost time {} ms", pjp.getSignature(), stopWatch.getElapse());
        } catch (Throwable e) { // 处理异常
            result = handlerException(pjp, e);
            log.info("[Execute Error] {} cost time {} ms", pjp.getSignature(), stopWatch.getElapse());
        } finally {
            UserContext.removeContext();
        }
        return result;
    }

    /**
     * 请求出现异常的处理控制
     *
     * @param pjp {@link ProceedingJoinPoint} 切点上下文
     * @param e   {@link Throwable} 异常信息
     * @return {@link RaxResult} 请求响应数据模型
     */
    private RaxResult<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        if (e instanceof DataAccessException) {
            String message = e.getMessage();
            message = message.substring(message.indexOf("Cause"));
            String caused = message.substring(0, message.indexOf("###"));
            return RaxResult.error(caused);
        }
        return RaxResult.error(e.getMessage());
    }

}
