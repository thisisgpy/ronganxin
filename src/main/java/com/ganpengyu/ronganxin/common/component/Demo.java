package com.ganpengyu.ronganxin.common.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/12
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        Date issueAt = new Date();
        Date expireAt = new Date(issueAt.getTime() + 15552000L);
        Algorithm algorithm = Algorithm.HMAC256("Jc^T9kN6bvBZH_Q3@PWU");
        String token = JWT.create()
                .withIssuer("ronganxin")
                .withIssuedAt(issueAt)
                .withExpiresAt(expireAt)
                .withClaim("userId", 1)
                .sign(algorithm);
        System.out.println(token);
    }

}
