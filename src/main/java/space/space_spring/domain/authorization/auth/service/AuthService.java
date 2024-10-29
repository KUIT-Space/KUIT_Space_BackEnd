package space.space_spring.domain.authorization.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.authorization.jwt.model.TokenPairDTO;
import space.space_spring.domain.authorization.jwt.model.TokenType;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.model.PostLoginDto;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.util.user.UserUtils;

import static space.space_spring.entity.enumStatus.UserSignupType.LOCAL;
import static space.space_spring.response.status.BaseExceptionResponseStatus.PASSWORD_NO_MATCH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final JwtLoginProvider jwtLoginProvider;
    private final JwtRepository jwtRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;


    @Transactional
    public PostLoginDto login(PostLoginDto.Request request) {
        // TODO 1. 이메일 존재 여부 확인(아이디 존재 여부 확인)
        User userByEmail = userUtils.findUserByEmail(request.getEmail(), LOCAL);
        log.info("userByEmail.getUserId: {}", userByEmail.getUserId());

        // TODO 2. 비밀번호 일치 여부 확인
        validatePassword(userByEmail, request.getPassword());

        // TODO 3. JWT 발급 -> access token, refresh token 2개 발급
        String accessToken = jwtLoginProvider.generateToken(userByEmail.getUserId(), TokenType.ACCESS);
        String refreshToken = jwtLoginProvider.generateToken(userByEmail.getUserId(), TokenType.REFRESH);

        // TODO 4. refresh token db에 저장
        TokenStorage tokenStorage = TokenStorage.builder()
                .user(userByEmail)
                .tokenValue(refreshToken)
                .build();
        jwtRepository.save(tokenStorage);

        // TODO 5. return
        TokenPairDTO tokenPairDTO = TokenPairDTO.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();

        return PostLoginDto.builder()
                .TokenPairDTO(tokenPairDTO)
                .userId(userByEmail.getUserId())
                .build();
    }

    private void validatePassword(User userByEmail, String password) {
        String encodePassword = userByEmail.getPassword();
        if(!passwordEncoder.matches(password,encodePassword)){
            throw new CustomException(PASSWORD_NO_MATCH);
        }

    }
}
