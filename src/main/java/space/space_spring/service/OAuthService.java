package space.space_spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import space.space_spring.dao.UserDao;
import space.space_spring.dto.jwt.TokenDTO;
import space.space_spring.dto.jwt.TokenType;
import space.space_spring.dto.oAuth.KakaoInfo;
import space.space_spring.entity.User;
import space.space_spring.exception.jwt.bad_request.JwtNoTokenException;
import space.space_spring.exception.jwt.bad_request.JwtUnsupportedTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtExpiredTokenException;
import space.space_spring.exception.jwt.unauthorized.JwtInvalidTokenException;
import space.space_spring.jwt.JwtLoginProvider;
import space.space_spring.util.user.UserUtils;

import static space.space_spring.entity.enumStatus.UserSignupType.KAKAO;
import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.response.status.BaseExceptionResponseStatus.UNSUPPORTED_TOKEN_TYPE;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserUtils userUtils;
    private final JwtLoginProvider jwtLoginProvider;
    private final UserDao userDao;

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 카카오 인증 서버가 전달해준 유저의 인가코드로 토큰 발급 요청
     */
    public String getAccessToken(String code, String clientId, String redirectUri, String clientSecret) throws JsonProcessingException {

        // TODO 1. HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // TODO 2. HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // TODO 3. 카카오 인증 서버로 HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // TODO 4. 카카오 인증 서버로부터의 HTTP 응답 (JSON) -> 액세스 토큰만 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * 카카오 인증 서버로부터 받은 access token 으로 해당 유저의 사용자 정보 가져오기
     */
    public KakaoInfo getKakaoInfo(String accessToken) throws JsonProcessingException {

        // TODO 1. HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // TODO 2. 카카오 api 서버로 HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // TODO 3. responseBody에 있는 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 유저의 이메일, nickname 정보 get
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return new KakaoInfo(nickname, email);
    }

    @Transactional
    public User findUserByOAuthInfo(KakaoInfo kakaoInfo) {
        String email = kakaoInfo.getEmail();
        String nickname = kakaoInfo.getNickName();

        // 카카오 서버로부터 얻은 정보로 회원가입 or 로그인
        return userUtils.findOrCreateUserForOAuthInfo(email, nickname, KAKAO);
    }

    public TokenDTO provideJwtToOAuthUser(User userByOAuthInfo) {
        String accessToken = jwtLoginProvider.generateToken(userByOAuthInfo, TokenType.ACCESS);
        String refreshToken = jwtLoginProvider.generateToken(userByOAuthInfo, TokenType.REFRESH);

        return new TokenDTO(accessToken, refreshToken);
    }

    @Transactional
    public void updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization-refresh");
        validateToken(token);
        return token.substring(JWT_TOKEN_PREFIX.length());

    }

    private void validateToken(String token) {
        if (token == null) {
            throw new JwtNoTokenException(TOKEN_NOT_FOUND);
        }
        if (!token.startsWith(JWT_TOKEN_PREFIX)) {
            throw new JwtUnsupportedTokenException(UNSUPPORTED_TOKEN_TYPE);
        }
    }

    @Transactional
    public void validateRefreshToken(String refreshToken) {
        // TODO 1. refresh token의 만료시간 체크
        if (jwtLoginProvider.isExpiredToken(refreshToken, TokenType.REFRESH)) {
            // refresh token이 만료된 경우 -> 예외 발생 -> 유저의 재 로그인 유도
            throw new JwtExpiredTokenException(EXPIRED_REFRESH_TOKEN);
        }

        // TODO 2. refresh token이 db에 실제로 존재하는지 체크
        if (userDao.findUserByRefreshToken(refreshToken).isEmpty()) {
            // refresh token이 db에 존재하지 않느 경우 -> 유효하지 않은 refresh token이므로 예외 발생
            throw new JwtInvalidTokenException(INVALID_TOKEN);
        }
    }

    @Transactional
    public TokenDTO updateTokenPair(String refreshToken) {
        // TODO 1. refresh token으로 user find
        Long userIdFromToken = jwtLoginProvider.getUserIdFromToken(refreshToken, TokenType.REFRESH);
        User userByUserId = userUtils.findUserByUserId(userIdFromToken);

        // TODO 2. new access token, refresh token 발급
        String newAccessToken = jwtLoginProvider.generateToken(userByUserId, TokenType.ACCESS);
        String newRefreshToken = jwtLoginProvider.generateToken(userByUserId, TokenType.REFRESH);

        // TODO 3. db의 refresh token update
        userByUserId.updateRefreshToken(newRefreshToken);

        // TODO 4. return
        return new TokenDTO(newRefreshToken, newAccessToken);
    }

}
