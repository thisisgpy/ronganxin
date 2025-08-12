package com.ganpengyu.ronganxin.common;

import com.ganpengyu.ronganxin.common.component.AuthRequired;
import com.ganpengyu.ronganxin.common.component.JwtService;
import com.ganpengyu.ronganxin.common.component.LocalCache;
import com.ganpengyu.ronganxin.common.component.LoginRequired;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

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

    @Resource
    private JwtService jwtService;

    @Value("${app.mode}")
    private String appMode;

    @Pointcut("execution(public com.ganpengyu.ronganxin.common.RaxResult *(..))")
    public void execute() {

    }

    /**
     * 切面逻辑，用于处理控制器方法的权限验证和执行监控。
     * 该方法通过 AOP 环绕通知，在目标方法执行前后进行如下操作：
     * 1. 解析方法上的 {@link AuthRequired} 注解，判断是否需要进行权限校验；
     * 2. 若需要权限校验，则从请求中获取 token，并从缓存中获取用户信息和资源权限列表；
     * 3. 校验用户是否存在及是否拥有访问所需权限；
     * 4. 在方法执行前后记录耗时，并在 finally 块中清理用户上下文。
     *
     * @param pjp {@link ProceedingJoinPoint} 切点上下文，包含被拦截方法的信息
     * @return {@link RaxResult} 请求响应数据模型，封装了业务结果或错误信息
     */
    @Around("execute()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        // 方法执行与异常处理
        RaxResult<?> result;
        StopWatch stopWatch = new StopWatch();
        try {
            if (appMode.equals("dev")) {
                LoginUserDto loginUser = new LoginUserDto();
                SysUserDto userInfo = new SysUserDto();
                userInfo.setId(1L);
                userInfo.setOrgId(1L);
                userInfo.setName("干鹏宇");
                userInfo.setMobile("15982338164");
                loginUser.setUserInfo(userInfo);
                UserContext.setContext(loginUser);
            } else {
                result = before(pjp);
                if (result != null) {
                    return result;
                }
                stopWatch.reset();
            }
            result = (RaxResult<?>) pjp.proceed();
            log.info("{} cost time {} ms", pjp.getSignature(), stopWatch.getElapse());
        } catch (Throwable e) { // 处理目标方法执行过程中抛出的异常
            result = handleException(pjp, e);
            log.info("[Execute Error] {} cost time {} ms", pjp.getSignature(), stopWatch.getElapse());
        } finally {
            // 清理线程上下文，防止内存泄漏
            UserContext.removeContext();
        }
        return result;
    }

    private RaxResult<?> before(ProceedingJoinPoint pjp) {
        LoginRequired loginRequired = getAnnotation(pjp, LoginRequired.class);
        if (null != loginRequired) {
            String token = getToken(pjp);
            // 检查 token 是否合法
            Long userId = jwtService.verifyToken(token);
            // 检查是否处于登录状态，非登录状态时缓存中没有 token
            String cachedToken = cache.get(Constants.getCacheUidTokenKey(userId));
            if (!StringUtils.hasText(cachedToken)) {
                return RaxResult.error("未授权访问");
            }
            String userJson = cache.get(Constants.getCacheTokenUserKey(token));
            if (StringUtils.hasText(userJson)) {
                SysUserDto user = JsonUtils.fromJson(userJson, SysUserDto.class);
                if (user == null) {
                    return RaxResult.error("未授权访问");
                }
                LoginUserDto loginUser = new LoginUserDto();
                loginUser.setUserInfo(user);
                loginUser.setToken(token);
                UserContext.setContext(loginUser);
            }

            // 权限校验逻辑
            AuthRequired authRequired = getAnnotation(pjp, AuthRequired.class);
            if (null != authRequired) {
                String resourceCodesJson = cache.get(Constants.getCacheResourceCodesKey(userId));
                if (StringUtils.hasText(resourceCodesJson)) {
                    List<String> resourceCodes = JsonUtils.fromJsonToList(resourceCodesJson, String.class);
                    if (resourceCodes == null) {
                        return RaxResult.error("未授权访问");
                    }
                    String[] requiredCodes = authRequired.value();
                    for (String requiredCode : requiredCodes) {
                        if (!resourceCodes.contains(requiredCode)) {
                            return RaxResult.error("未授权访问");
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 请求出现异常的处理控制
     *
     * @param pjp {@link ProceedingJoinPoint} 切点上下文
     * @param e   {@link Throwable} 异常信息
     * @return {@link RaxResult} 请求响应数据模型
     */
    private RaxResult<?> handleException(ProceedingJoinPoint pjp, Throwable e) {
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

    private <A extends Annotation> A getAnnotation(ProceedingJoinPoint pjp, Class<A> annotationClass) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取 Method 对象
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }


}
