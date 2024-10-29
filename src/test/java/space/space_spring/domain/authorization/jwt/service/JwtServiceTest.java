package space.space_spring.domain.authorization.jwt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import space.space_spring.domain.authorization.jwt.model.JwtLoginProvider;
import space.space_spring.domain.authorization.jwt.model.TokenResolver;
import space.space_spring.domain.authorization.jwt.repository.JwtRepository;
import space.space_spring.domain.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {JwtService.class, JwtRepository.class, UserRepository.class, JwtLoginProvider.class, TokenResolver.class})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtRepository jwtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtLoginProvider jwtLoginProvider;

    @Autowired
    private TokenResolver tokenResolver;

    @Test
    @DisplayName("")
    void test() throws Exception {
        //given

        //when

        //then
    }


}