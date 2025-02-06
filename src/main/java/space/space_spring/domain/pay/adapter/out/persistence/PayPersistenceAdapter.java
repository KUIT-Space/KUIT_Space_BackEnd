package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements CreatePayPort {

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
}
