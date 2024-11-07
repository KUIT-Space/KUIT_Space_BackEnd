package space.space_spring.domain.pay.service;

import ch.qos.logback.core.testUtil.XTeeOutputStream;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.pay.repository.PayRequestRepository;
import space.space_spring.domain.pay.repository.PayRequestTargetRepository;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.entity.enumStatus.UserSignupType;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({PayService.class})
@ActiveProfiles("test")
@EnableJpaRepositories(basePackageClasses = {PayRequestRepository.class, PayRequestTargetRepository.class, UserRepository.class})
@EntityScan(basePackageClasses = {PayRequest.class, PayRequestTarget.class, User.class})
class PayServiceTest {

    @Autowired
    private PayService payService;

    @Autowired
    private PayRequestRepository payRequestRepository;

    @Autowired
    private PayRequestTargetRepository payRequestTargetRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저가 요청한 정산 중, 현재 진행 중인 정산 정보들과 유저가 요청받은 정산 중, 현재 진행 중인 정산 정보들을 return 한다.")
    void getPayHomeInfos() throws Exception {
        //given
        User user = User.create("email", "password", "name", UserSignupType.LOCAL);
        User savedUser = userRepository.save(user);



        //when

        //then
    }

}