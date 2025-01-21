package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.CreatePayUseCase;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {


    @Override
    @Transactional
    public void createPay(CreatePayCommand command) {

        // 정산 생성자, 정산 타겟 멤버들을 모두 찾고
        // SpaceMember, SpaceMemberJpaEntity 분리해야 함


        // 이 사람들이 모두 같은 스페이스에 있는지 확인하고
        // 이건 일급 컬렉션 생성 & 이 스페이스 멤버들이 모두 같은 스페이스에 속하는지 검사하는 메서드 생성해서 처리하면 될 듯


        // 정산 요청 금액의 유효성을 검증하고


        // 정산, 정산 타겟 모델 생성

    }
}
