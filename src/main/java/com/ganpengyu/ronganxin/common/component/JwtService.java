package com.ganpengyu.ronganxin.common.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ganpengyu.ronganxin.common.RaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/5
 */
@Slf4j
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    /**
     * 创建JWT token
     *
     * @param userId 用户ID，用于在token中存储用户标识
     * @return 生成的JWT token字符串
     */
    public String createToken(Long userId) {
        // 设置token签发时间和过期时间
        Date issueAt = new Date();
        Date expireAt = new Date(issueAt.getTime() + expiration);
        try {
            // 使用HMAC256算法和密钥创建签名算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 构建并返回JWT token
            return JWT.create()
                    .withIssuer("ronganxin")
                    .withIssuedAt(issueAt)
                    .withExpiresAt(expireAt)
                    .withClaim("userId", userId)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RaxException("创建 token 失败。" + e.getMessage());
        }
    }

    /**
     * 验证JWT token并获取用户ID
     *
     * @param token 待验证的JWT token字符串
     * @return 返回token中包含的用户ID
     * @throws RaxException 当token校验失败时抛出异常
     */
    public Long verifyToken(String token) {
        try {
            // 使用HMAC256算法和密钥创建签名算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 验证token
            JWT.require(algorithm)
                    .withIssuer("ronganxin")
                    .build()
                    .verify(token);
            // 获取用户ID
            return JWT.decode(token).getClaim("userId").asLong();
        } catch (Exception e) {
            log.error("token 校验失败", e);
            throw new RaxException("token 校验失败");
        }
    }


}
