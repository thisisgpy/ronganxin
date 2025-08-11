package com.ganpengyu.ronganxin.common;

import com.ganpengyu.ronganxin.common.component.Auth;
import com.ganpengyu.ronganxin.common.component.LocalCache;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.JsonUtils;
import com.ganpengyu.ronganxin.common.util.StopWatch;
import com.ganpengyu.ronganxin.web.dto.user.LoginUserDto;
import com.ganpengyu.ronganxin.web.dto.user.SysUserDto;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

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

    @Resource
    private LocalCache<String, String> cache;

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
        Auth auth = getAuthAnnotation(pjp);
        if (null != auth) {
            String token = getToken(pjp);
            String userJson = cache.get(Constants.getCacheTokenUserKey(token));
            if (StringUtils.hasText(userJson)) {
                SysUserDto user = JsonUtils.fromJson(userJson, SysUserDto.class);
                LoginUserDto loginUser = new LoginUserDto();
                loginUser.setUserInfo(user);
                loginUser.setToken(token);
                UserContext.setContext(loginUser);
            } else {
                return RaxResult.error("未授权访问");
            }
        }
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

    /**
     * 从HTTP请求中获取认证令牌
     *
     * @param pjp 切点对象，用于获取当前请求上下文
     * @return 返回Authorization请求头中的令牌值，如果无法获取则返回null
     */
    private String getToken(ProceedingJoinPoint pjp) {
        // 获取当前请求的Servlet属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 示例：获取特定请求头（如 Authorization）
            return request.getHeader("Authorization");
        } else {
            return null;
        }
    }

    /**
     * 获取方法上的Auth注解
     *
     * @param pjp 连接点对象，包含目标方法的信息
     * @return 返回方法上的Auth注解对象，如果方法上没有该注解则返回null
     */
    private Auth getAuthAnnotation(ProceedingJoinPoint pjp) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取 Method 对象
        Method method = signature.getMethod();
        // 获取指定注解（例如自定义注解 MyAnnotation）
        return method.getAnnotation(Auth.class);
    }


}
