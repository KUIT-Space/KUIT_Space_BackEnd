package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
import space.space_spring.domain.pay.application.port.in.createPay.TargetOfCreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayAmountPolicy;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMembers;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {

    private final CreatePayPort createPayPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    @Transactional
    public Long createPay(CreatePayCommand command) {
        // 정산 생성자 로드 & PayRequest 도메인 엔티티 생성
        SpaceMember payCreator = loadSpaceMemberPort.loadSpaceMember(command.getPayCreatorId());
        PayRequest payRequest = createPayRequest(command, payCreator);

        // 정산 타겟 멤버들 로드 & PayRequestTarget 도메인 엔티티들 생성
        List<PayRequestTarget> payRequestTargets = createPayRequestTargets(command, payRequest);

        // 정산 생성자, 정산 대상자들이 같은 스페이스에 존재하는지 검증
        validateSpaceMembers(payCreator, payRequestTargets);

        // PayAmountPolicy 유효성 검증
        validatePayAmountPolicy(command, payRequestTargets);

        // 모두 문제없으면 저장
        return createPayPort.savePay(payRequest, payRequestTargets);
    }

    private void validatePayAmountPolicy(CreatePayCommand command, List<PayRequestTarget> payRequestTargets) {
        PayAmountPolicy payAmountPolicy = command.getPayType().getPayAmountPolicy();

        List<Money> targetAmounts = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            targetAmounts.add(payRequestTarget.getRequestedAmount());
        }

        payAmountPolicy.validatePayAmount(command.getTotalAmount(), targetAmounts);
    }

    private void validateSpaceMembers(SpaceMember payCreator, List<PayRequestTarget> payRequestTargets) {
        List<SpaceMember> allOfSpaceMember = new ArrayList<>();
        allOfSpaceMember.add(payCreator);

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            allOfSpaceMember.add(payRequestTarget.getTargetMember());
        }

        SpaceMembers spaceMembers = SpaceMembers.of(allOfSpaceMember);
        try {
            spaceMembers.validateMembersInSameSpace();
        } catch (IllegalStateException e) {
            throw new CustomException(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE);
        }
    }

    private List<PayRequestTarget> createPayRequestTargets(CreatePayCommand command, PayRequest payRequest) {
        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            SpaceMember targetMember = loadSpaceMemberPort.loadSpaceMember(target.getTargetMemberId());
            // 수정 필요
            PayRequestTarget payRequestTarget = PayRequestTarget.createNewPayRequestTarget(1L, targetMember, payRequest, target.getRequestedAmount());
            payRequestTargets.add(payRequestTarget);
        }
        return payRequestTargets;
    }

    private PayRequest createPayRequest(CreatePayCommand command, SpaceMember payCreator) {
        // 수정 필요
        return PayRequest.createNewPayRequest(1L, payCreator, command.getTotalAmount(), command.getTotalTargetNum(), command.getBank(), command.getPayType());
    }
}
