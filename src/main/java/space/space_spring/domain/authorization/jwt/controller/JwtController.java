package space.space_spring.domain.authorization.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.authorization.jwt.service.JwtService;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
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

        TokenPairDTO newTokenPairDTO = jwtService.updateTokenPair(request);

        response.setHeader("Authorization-refresh", "Bearer " + newTokenPairDTO.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + newTokenPairDTO.getAccessToken());

        System.out.println("tokenPairDTO.getAccessToken() = " + newTokenPairDTO.getAccessToken());
        System.out.println("tokenPairDTO.getRefreshToken() = " + newTokenPairDTO.getRefreshToken());

        // return
        return new BaseResponse<>("토큰 갱신 요청 성공");
    }
}
