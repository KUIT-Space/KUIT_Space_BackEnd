package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMemberJpaEntity;
import space.space_spring.domain.spaceMember.SpringDataSpaceMemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements CreatePayPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestMapper payRequestMapper;
    private final PayRequestTargetMapper payRequestTargetMapper;

    /**
     * 예외처리 enum 메시지 다시 작성
     */
    @Override
    public Long savePay(PayRequest payRequest, List<PayRequestTarget> payRequestTargets) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findById(payRequest.getPayCreator().getId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 spaceMemberId : " + payRequest.getPayCreator().getId())
        );
        PayRequestJpaEntity payRequestJpaEntity = payRequestMapper.toJpaEntity(payCreatorJpaEntity, payRequest);
        PayRequestJpaEntity savedPayRequestJpaEntity = payRequestRepository.save(payRequestJpaEntity);

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findById(payRequestTarget.getTargetMember().getId()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 spaceMemberId : " + payRequest.getPayCreator().getId())
            );
            payRequestTargetRepository.save(payRequestTargetMapper.toJpaEntity(targetMemberJpaEntity, payRequestJpaEntity, payRequestTarget));
        }

        return savedPayRequestJpaEntity.getId();
    }
}
