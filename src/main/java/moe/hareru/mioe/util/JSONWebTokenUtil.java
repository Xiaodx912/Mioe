package moe.hareru.mioe.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JSONWebTokenUtil {

    private final static long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    public static String sign(Long userId, String secret) {
        return sign(userId, secret, EXPIRE_TIME);
    }

    public static String sign(Long userId, String secret, Long expireTimeMs) {
        expireTimeMs = expireTimeMs == null ? EXPIRE_TIME : expireTimeMs;
        Date signDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + expireTimeMs);
        try {
            return JWT.create()
                    .withClaim("userid", userId)
                    .withIssuedAt(signDate)
                    .withExpiresAt(expireDate)
                    .sign(Algorithm.HMAC256(secret));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean verify(String token, Long userId, String secret){
        return verify(token, userId, secret, 0L);
    }

    public static boolean verify(String token, Long userId, String secret, Long revokeTimeMs) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withClaim("userid", userId)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            assert jwt.getIssuedAt().compareTo(new Date(revokeTimeMs)) < 0;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userid").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getUserIdStr(String token) {
        Long userId = getUserId(token);
        return (userId == null) ? null : userId.toString();
    }
}
