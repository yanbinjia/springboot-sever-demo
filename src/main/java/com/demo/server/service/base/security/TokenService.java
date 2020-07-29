package com.demo.server.service.base.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.vo.Token;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.exception.AppException;
import com.demo.server.common.util.JwtUtil;
import com.demo.server.common.util.RandomUtil;
import com.demo.server.config.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenService {
    @Autowired
    JwtConfig jwtConfig;

    @PostConstruct // 初始化执行顺序:构造方法->@Autowired->@PostConstruct
    public void init() {
    }

    /**
     * 校验token
     */
    public Result<String> checkToken(String userId, String token) {
        Result<String> result = new Result<>(ResultCode.SEC_TOKEN_ERROR);

        if (StringUtils.isAnyBlank(userId, token)) {
            log.warn("checkToken, param error, userId={},token={},", userId, token);
            result.setResultCode(ResultCode.SEC_TOKEN_PARAM);
            return result;
        }

        token = tokenStrProcess(token);

        // 基本解析
        DecodedJWT jwt = null;
        String userIdInToken = "", tokenFor = "";

        jwt = JwtUtil.decodeToken(token);

        if (jwt != null) {
            userIdInToken = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).asString();
            tokenFor = jwt.getClaim(AppConstant.JWT_CLAIM_FOR).asString();
        } else {
            result.setResultCode(ResultCode.SEC_TOKEN_ERROR);
            log.warn("checkToken, decodeToken error, userId={},token={}", userId, token);
            return result;
        }

        // tokenFor校验,是否为access
        if (!StringUtils.equals(tokenFor, AppConstant.JWT_CLAIM_FOR_ACC)) {
            result.setResultCode(ResultCode.SEC_TOKEN_MISS_TYPE);
            log.warn("checkToken, token中tokenFor不匹配, userId={},tokenFor={}", userId, tokenFor);
            return result;
        }

        // token中uid与请求参数userId不匹配! 防止越权
        if (!StringUtils.equals(userId, userIdInToken)) {
            result.setResultCode(ResultCode.SEC_TOKEN_MISS_UID);
            log.warn("checkToken, token中uid与请求参数中uid不匹配, userId={},userIdInToken={}", userId, userIdInToken);
            return result;
        }

        // 验证
        DecodedJWT jwtForVerify = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwtForVerify = verifier.verify(token);
        } catch (TokenExpiredException e) {
            // 过期
            result.setResultCode(ResultCode.SEC_TOKEN_EXPIRE);
            return result;
        } catch (Exception e) {
            // 失败
            log.warn("checkToken JWT verifyToken fail : {}", token, e);
        }

        if (jwtForVerify != null) {
            // 验证成功
            result.setResultCode(ResultCode.SUCCESS);
        }

        return result;
    }

    /**
     * 登录成功后，生成Token(accessToken&refreshToken)
     */
    public Token createToken(String userId) {

        if (StringUtils.isBlank(userId)) {
            throw new AppException(ResultCode.SEC_TOKEN_PARAM);
        }

        Token token = this.buildToken(userId);

        return token;
    }

    /**
     * 使用refreshToken，刷新生成新的accessToken
     */
    public Token refreshToken(String userId, String refreshToken) {

        if (StringUtils.isAnyBlank(userId, refreshToken)) {
            throw new AppException(ResultCode.SEC_TOKEN_PARAM);
        }

        // -----------------------------------------------
        // 校验refreshToken
        // 基本解析
        DecodedJWT jwt = null;
        String userIdInToken = "", tokenFor = "", sign = "";

        jwt = JwtUtil.decodeToken(refreshToken);

        if (jwt != null) {
            userIdInToken = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).asString();
            tokenFor = jwt.getClaim(AppConstant.JWT_CLAIM_FOR).asString();
            sign = jwt.getSignature();
        } else {
            log.warn("refreshToken, decodeToken error, userId={},token={}", userId, refreshToken);
            throw new AppException(ResultCode.SEC_TOKEN_PARAM);
        }

        // tokenFor校验,是否为refresh
        if (!StringUtils.equals(tokenFor, AppConstant.JWT_CLAIM_FOR_REF)) {
            log.warn("refreshToken, token中tokenFor不匹配, userId={},tokenFor={}", userId, tokenFor);
            throw new AppException(ResultCode.SEC_TOKEN_MISS_TYPE);
        }

        // token中uid与请求参数userId不匹配! 防止越权
        if (!StringUtils.equals(userId, userIdInToken)) {
            log.warn("refreshToken, token中uid与请求参数中uid不匹配, userId={},userIdInToken={}", userId, userIdInToken);
            throw new AppException(ResultCode.SEC_TOKEN_MISS_UID);
        }

        // 验证
        DecodedJWT jwtForVerify = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwtForVerify = verifier.verify(refreshToken);
        } catch (TokenExpiredException e) {
            // 过期
            log.warn("refreshToken JWT verifyToken fail : {}", refreshToken, e);
            throw new AppException(ResultCode.SEC_TOKEN_EXPIRE);
        } catch (Exception e) {
            // 验证失败
            log.warn("refreshToken JWT verifyToken fail : {}", refreshToken, e);
            throw new AppException(ResultCode.SEC_TOKEN_ERROR);
        }

        // TODO: 存储中检查refreshToken是否存在？是否过期？是否禁用？
        if (jwtForVerify != null) {
            sign = sign + "";
        }
        // -----------------------------------------------
        // 检查通过后生成,重新生成Token
        Token token = this.buildRefreshToken(userId, refreshToken);

        return token;
    }

    /*
     * HTTP请求的头信息 Authorization:Bearer <token> , 解析出<token>
     *
     */
    private String tokenStrProcess(String token) {
        String tokenAferProcess = token;
        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer")) {
            String[] tokenArray = StringUtils.splitByWholeSeparator(token, null);
            if (tokenArray.length >= 2) {
                tokenAferProcess = tokenArray[1].trim();
            }
        }
        return tokenAferProcess;
    }

    private Token buildToken(String userId) {
        String accessTokenStr = this.createAccessToken(userId);
        String refreshTokenStr = this.createRefreshToken(userId);

        Token token = new Token();
        token.setUserId(userId);
        token.setAccessToken(accessTokenStr);
        token.setRefreshToken(refreshTokenStr);

        // TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

        return token;
    }

    private Token buildRefreshToken(String userId, String refreshToken) {
        String accessToken = this.createAccessToken(userId);

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);

        // TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

        return token;
    }

    private String createAccessToken(String userId) {
        return this.createJwtToken(userId, 1);
    }

    private String createRefreshToken(String userId) {
        return this.createJwtToken(userId, 2);
    }

    private String createJwtToken(String userId, int type) {
        // The "jti" (JWT ID) claim provides a unique identifier for the JWT. The "jti"
        // claim can be used to prevent the JWT from being replayed.
        // 可以用来标识和记录颁发的jwt
        String jwtId = this.generateJwtId();

        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put(AppConstant.JWT_CLAIM_USER_ID, userId);
        claimsMap.put(PublicClaims.JWT_ID, jwtId);
        claimsMap.put(PublicClaims.ISSUER, jwtConfig.getIss());

        int expireAfterNSecs = 3600;

        switch (type) {
            case 1:
                // access token
                claimsMap.put(AppConstant.JWT_CLAIM_FOR, AppConstant.JWT_CLAIM_FOR_ACC);
                expireAfterNSecs = jwtConfig.getExpireAfterSecs();
                break;
            case 2:
                // refresh token
                claimsMap.put(AppConstant.JWT_CLAIM_FOR, AppConstant.JWT_CLAIM_FOR_REF);
                expireAfterNSecs = jwtConfig.getRefreshExpireAfterSecs();
                break;
            default:
                // access token
                claimsMap.put(AppConstant.JWT_CLAIM_FOR, AppConstant.JWT_CLAIM_FOR_ACC);
                expireAfterNSecs = jwtConfig.getExpireAfterSecs();
                break;
        }

        String jwtStr = JwtUtil.createToken(claimsMap, jwtConfig.getSecretKey(), expireAfterNSecs);

        return jwtStr;
    }

    private String generateJwtId() {
        return RandomUtil.uuidWithoutSeparator();
    }
}
