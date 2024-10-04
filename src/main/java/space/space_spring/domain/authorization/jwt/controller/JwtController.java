package space.space_spring.domain.authorization.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.authorization.jwt.service.JwtService;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
import space.space_spring.entity.User;
import space.space_spring.response.BaseResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;

    /**
     * 엑세스 토큰 갱신 요청 처리
     * -> 엑세스 토큰, 리프레시 토큰 갱신 (RTR 패턴)
     */
    @PostMapping("/new-token")
    public BaseResponse<String> updateAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // access token, refresh token 파싱
        TokenPairDTO tokenPairDTO  = jwtService.resolveTokenPair(request);

        // access token 로부터 user find
        User userByAccessToken = jwtService.getUserByAccessToken(tokenPairDTO.getAccessToken());

        // refresh token 유효성 검사
        jwtService.validateRefreshToken(userByAccessToken, tokenPairDTO.getRefreshToken());

        // access token, refresh token 새로 발급
        TokenPairDTO newTokenPairDTO = jwtService.updateTokenPair(userByAccessToken);

        // response header에 새로 발급한 token pair set
        response.setHeader("Authorization-refresh", "Bearer " + newTokenPairDTO.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + newTokenPairDTO.getAccessToken());

        System.out.println("tokenPairDTO.getAccessToken() = " + newTokenPairDTO.getAccessToken());
        System.out.println("tokenPairDTO.getRefreshToken() = " + newTokenPairDTO.getRefreshToken());

        // return
        return new BaseResponse<>("토큰 갱신 요청 성공");
    }
}
