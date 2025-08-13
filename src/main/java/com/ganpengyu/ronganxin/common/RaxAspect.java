package com.ganpengyu.ronganxin.common;

import com.ganpengyu.ronganxin.common.component.AuthMatchType;
import com.ganpengyu.ronganxin.common.component.AuthRequired;
import com.ganpengyu.ronganxin.common.component.JwtService;
import com.ganpengyu.ronganxin.common.component.RedisService;
import com.ganpengyu.ronganxin.common.context.UserContext;
import com.ganpengyu.ronganxin.common.util.JsonUtils;
import com.ganpengyu.ronganxin.common.util.StopWatch;
import com.ganpengyu.ronganxin.service.AuthService;
import com.ganpengyu.ronganxin.service.UserService;
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
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private RedisService redisService;

    @Resource
    private JwtService jwtService;

    @Value("${app.mode}")
    private String appMode;

    @Value("${app.devToken}")
    private String devToken;

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
            // 开发模式下设置默认用户上下文
            if (appMode.equals("dev")) {
                SysUserDto userInfo = this.getUserInfo(null);
                LoginUserDto loginUser = new LoginUserDto();
                loginUser.setUserInfo(userInfo);
                UserContext.setContext(loginUser);
            } else {
                // 非开发模式下执行前置权限校验逻辑
                result = before(pjp);
                if (result != null) {
                    return result;
                }
                stopWatch.reset();
            }

            // 执行目标方法并记录耗时
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


    /**
     * 在方法执行前进行权限验证处理
     *
     * @param pjp 切点对象，包含被拦截方法的信息
     * @return 验证失败时返回错误结果，验证通过时返回null
     * @throws Exception 处理过程中可能抛出的异常
     */
    private RaxResult<?> before(ProceedingJoinPoint pjp) throws Exception {
        // 获取方法上的 @AuthRequired 注解，若不存在则跳过权限验证
        AuthRequired authRequired = getAnnotation(pjp, AuthRequired.class);
        if (null == authRequired) {
            return null;
        }

        // 从请求中提取 token
        String token = getToken(pjp);

        // 检查 token 是否合法，并解析出用户ID
        Long userId = jwtService.verifyToken(token);

        // 检查用户是否处于登录状态（缓存中是否存在有效 token）
        String cachedToken = getCachedToken(userId);
        if (!StringUtils.hasText(cachedToken)) {
            return RaxResult.error("未授权访问");
        }

        // 获取或加载用户信息
        SysUserDto user = getUserInfo(userId);
        if (null == user) {
            user = userService.findUserDtoById(userId);
        }

        // 将用户信息设置到当前线程的上下文环境中
        LoginUserDto loginUser = new LoginUserDto();
        loginUser.setUserInfo(user);
        loginUser.setToken(token);
        UserContext.setContext(loginUser);

        // 获取注解中定义的权限码和匹配规则
        String[] requiredCodes = authRequired.value();
        AuthMatchType authMatchType = authRequired.matchType();

        // 如果匹配类型为 NONE，表示不需权限校验，直接放行
        if (authMatchType.equals(AuthMatchType.NONE)) {
            return null;
        }

        // 查询用户拥有的所有资源权限码
        List<String> resourceCodes = authService.findResourceCodesByUserId(userId);

        // 根据匹配规则判断用户是否有权限访问
        boolean allowed = false;
        for (String requiredCode : requiredCodes) {
            if (!resourceCodes.contains(requiredCode)) {
                // 若是全匹配模式（ALL），任意一个权限码不匹配即拒绝访问
                if (authMatchType.equals(AuthMatchType.ALL)) {
                    return RaxResult.error("未授权访问");
                }
            } else {
                // 若是任一匹配模式（ANY），只要有一个权限码匹配即可
                allowed = true;
            }
        }

        // 若没有任何权限码匹配且不是 ALL 模式，则拒绝访问
        if (!allowed) {
            return RaxResult.error("未授权访问");
        }

        // 权限验证通过，返回 null 表示继续执行原方法
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
        if (appMode.equals("dev")) {
            return devToken;
        }
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

    private SysUserDto getUserInfo(Long userId) {
        if (appMode.equals("dev")) {
            String json = "{\"id\":1,\"orgId\":10000,\"mobile\":\"15982338164\",\"name\":\"干鹏宇\",\"gender\":null,\"idCard\":null,\"isDefaultPassword\":null,\"status\":null,\"isDeleted\":null,\"createTime\":null,\"createBy\":null,\"updateTime\":null,\"updateBy\":null}\n";
            return JsonUtils.fromJson(json, SysUserDto.class);
        }
        return redisService.get(Constants.getAuthUserInfoKey(userId), SysUserDto.class);
    }

    private String getCachedToken(Long userId) {
        return redisService.get(Constants.getAuthTokenKey(userId), String.class);
    }


    private <A extends Annotation> A getAnnotation(ProceedingJoinPoint pjp, Class<A> annotationClass) throws Exception {
        Class<?> targetClass = pjp.getTarget().getClass();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 获取 Method 对象
        Method method = targetClass.getMethod(signature.getName(), signature.getParameterTypes());
        return method.getAnnotation(annotationClass);
    }


}
