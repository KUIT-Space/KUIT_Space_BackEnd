package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.domain.spaceMember.SpringDataSpaceMemberRepository;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements CreatePayPort, LoadPayRequestPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestMapper payRequestMapper;
    private final PayRequestTargetMapper payRequestTargetMapper;

    /**
     * 저장 성공한 PayRequestJpaEntity의 PK값 return
     */
    @Override
    public Long savePay(PayRequest payRequest, List<PayRequestTarget> payRequestTargets) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findById(payRequest.getPayCreator().getId()).orElseThrow(
                () -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        PayRequestJpaEntity payRequestJpaEntity = payRequestMapper.toJpaEntity(payCreatorJpaEntity, payRequest);
        PayRequestJpaEntity savedPayRequestJpaEntity = payRequestRepository.save(payRequestJpaEntity);

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findById(payRequestTarget.getTargetMember().getId()).orElseThrow(
                    () -> new CustomException(SPACE_MEMBER_NOT_FOUND));
            payRequestTargetRepository.save(payRequestTargetMapper.toJpaEntity(targetMemberJpaEntity, payRequestJpaEntity, payRequestTarget));
        }

        return savedPayRequestJpaEntity.getId();
    }

    @Override
    public List<PayRequest> findListByCreator(SpaceMember payCreator) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findById(payCreator.getId()).orElseThrow(
                () -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        Optional<List<PayRequestJpaEntity>> byPayCreator = payRequestRepository.findByPayCreator(payCreatorJpaEntity);

        if (byPayCreator.isEmpty()) return new ArrayList<>();

        List<PayRequest> payRequests = new ArrayList<>();
        for (PayRequestJpaEntity payRequestJpaEntity : byPayCreator.get()) {
            payRequests.add(payRequestMapper.toDomainEntity(payCreator, payRequestJpaEntity));
        }

        return payRequests;
    }
}
