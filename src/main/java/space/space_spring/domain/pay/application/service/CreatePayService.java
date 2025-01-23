package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.CreatePayUseCase;
import space.space_spring.domain.pay.application.port.in.TargetOfCreatePayDto;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayAmountPolicy;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {

    private final CreatePayPort createPayPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    @Transactional
    public void createPay(CreatePayCommand command) {

        // 정산 생성자, 정산 타겟 멤버들을 모두 찾고, (& 찾은 사람들로 도메인 엔티티 생성)
        SpaceMember payCreator = loadSpaceMemberPort.loadSpaceMember(command.getPayCreatorId());
        PayRequest payRequest = PayRequest.create(payCreator, command.getTotalAmount(), command.getBank(), command.getPayType());

        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (TargetOfCreatePayDto target : command.getTargets()) {
            SpaceMember targetMember = loadSpaceMemberPort.loadSpaceMember(target.getTargetMemberId());
            PayRequestTarget payRequestTarget = PayRequestTarget.create(targetMember, payRequest, target.getRequestedAmount());
            payRequestTargets.add(payRequestTarget);
        }

        // 이 사람들이 모두 같은 스페이스에 있는지 확인하고,
        // (이건 일급 컬렉션 생성 & 이 스페이스 멤버들이 모두 같은 스페이스에 속하는지 검사하는 메서드 생성해서 처리하면 될 듯)
        List<SpaceMember> spaceMembers = new ArrayList<>();
        spaceMembers.add(payCreator);
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            spaceMembers.add(payRequestTarget.getTargetMember());
        }

        Long spaceId = spaceMembers.get(0).getSpace().getId();          // 일단 현재 Space 도 도메인 엔티티와 JPA 분리 안되어 있어서 일단 이렇게 구현
        for (SpaceMember spaceMember : spaceMembers) {
            if (!Objects.equals(spaceMember.getSpace().getId(), spaceId)) {
                throw new IllegalArgumentException("에러, 정산 생성자와 정산 대상이 같은 스페이스에 속하지 않습니다");
            }
        }

        // 정산 요청 금액의 유효성을 검증하고,
        List<Money> targetAmounts = new ArrayList<>();
        for (TargetOfCreatePayDto target : command.getTargets()) {
            targetAmounts.add(target.getRequestedAmount());
        }

        PayAmountPolicy payAmountPolicy = command.getPayType().getPayAmountPolicy();
        payAmountPolicy.validatePayAmount(command.getTotalAmount(), targetAmounts);

        // 모두 문제없으면 정산, 정산 타겟 모델 생성 & 저장
        createPayPort.savePay(payRequest, payRequestTargets);
    }
}
