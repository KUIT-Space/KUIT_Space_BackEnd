package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestTargetJpaEntity;
import space.space_spring.domain.pay.adapter.out.persistence.mapper.PayRequestTargetMapper;
import space.space_spring.domain.pay.application.port.out.*;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@RequiredArgsConstructor
@Repository
public class PayRequestTargetPersistenceAdapter implements CreatePayRequestTargetPort, LoadPayRequestTargetPort, UpdatePayPort, DeletePayRequestTargetPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestTargetMapper payRequestTargetMapper;

    @Override
    public List<PayRequestTarget> createPayRequestTargets(List<PayRequestTarget> payRequestTargets) {
        List<PayRequestTarget> savedList = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findByIdAndStatus(payRequestTarget.getTargetMemberId(), BaseStatusType.ACTIVE)
                    .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

            PayRequestJpaEntity payRequestJpaEntity  = payRequestRepository.findByIdAndStatus(payRequestTarget.getPayRequestId(), BaseStatusType.ACTIVE)
                    .orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));

            PayRequestTargetJpaEntity save = payRequestTargetRepository.save(payRequestTargetMapper.toJpaEntity(targetMemberJpaEntity, payRequestJpaEntity, payRequestTarget));
            savedList.add(payRequestTargetMapper.toDomainEntity(save));
        }

        return savedList;
    }

    @Override
    public List<PayRequestTarget> loadByTargetMemberId(Long targetMemberId) {
        SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findByIdAndStatus(targetMemberId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        Optional<List<PayRequestTargetJpaEntity>> byTargetMember = payRequestTargetRepository.findListByTargetMemberAndStatus(targetMemberJpaEntity, BaseStatusType.ACTIVE);

        if (byTargetMember.isEmpty()) return new ArrayList<>();

        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (PayRequestTargetJpaEntity payRequestTargetJpaEntity : byTargetMember.get()) {
            payRequestTargets.add(payRequestTargetMapper.toDomainEntity(payRequestTargetJpaEntity));
        }

        return payRequestTargets;
    }

    @Override
    public List<PayRequestTarget> loadByPayRequestId(Long payRequestId) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findByIdAndStatus(payRequestId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));

        Optional<List<PayRequestTargetJpaEntity>> byPayRequest = payRequestTargetRepository.findByPayRequestAndStatus(payRequestJpaEntity, BaseStatusType.ACTIVE);

        if (byPayRequest.isEmpty()) {
            return new ArrayList<>();
        }

        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (PayRequestTargetJpaEntity payRequestTargetJpaEntity : byPayRequest.get()) {
            payRequestTargets.add(payRequestTargetMapper.toDomainEntity(payRequestTargetJpaEntity));
        }

        return Collections.unmodifiableList(payRequestTargets);
    }

    @Override
    public PayRequestTarget loadById(Long payRequestTargetId) {
        PayRequestTargetJpaEntity jpaEntity = payRequestTargetRepository.findByIdAndStatus(payRequestTargetId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_TARGET_NOT_FOUND));

        return payRequestTargetMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public void update(PayRequestTarget payRequestTarget) {
        PayRequestTargetJpaEntity jpaEntity = payRequestTargetRepository.findByIdAndStatus(payRequestTarget.getId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_TARGET_NOT_FOUND));

        jpaEntity.changeCompletionStatus(payRequestTarget.isComplete());
    }

    @Override
    public void deleteAllPayRequestTarget(List<Long> payRequestTargetIds) {
        List<PayRequestTargetJpaEntity> allById = payRequestTargetRepository.findAllByIdInAndStatus(payRequestTargetIds, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS));

        /**
         * 정산 타겟들이 하나도 없는 경우 -> 예외 메시지 throw
         * 에러 메시지 내용 좀 더 고민해보기
         */

        for (PayRequestTargetJpaEntity payRequestTargetJpaEntity : allById) {
            payRequestTargetJpaEntity.updateToInactive();
        }
    }
}
