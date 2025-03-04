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
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@RequiredArgsConstructor
@Repository
public class PayRequestTargetPersistenceAdapter implements CreatePayRequestTargetPort, LoadPayRequestTargetPort, UpdatePayPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestTargetMapper payRequestTargetMapper;

    @Override
    public List<PayRequestTarget> createPayRequestTargets(List<PayRequestTarget> payRequestTargets) {
        List<PayRequestTarget> savedList = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findById(payRequestTarget.getTargetMemberId()).orElseThrow(
                    () -> new CustomException(SPACE_MEMBER_NOT_FOUND));

            PayRequestJpaEntity payRequestJpaEntity  = payRequestRepository.findById(payRequestTarget.getPayRequestId()).orElseThrow(
                    () -> new CustomException(PAY_REQUEST_NOT_FOUND));

            PayRequestTargetJpaEntity save = payRequestTargetRepository.save(payRequestTargetMapper.toJpaEntity(targetMemberJpaEntity, payRequestJpaEntity, payRequestTarget));
            savedList.add(payRequestTargetMapper.toDomainEntity(save));
        }

        return savedList;
    }

    @Override
    public List<PayRequestTarget> loadByTargetMemberId(Long targetMemberId) {
        SpaceMemberJpaEntity targetMemberJpaEntity = spaceMemberRepository.findById(targetMemberId).orElseThrow(
                () -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        Optional<List<PayRequestTargetJpaEntity>> byTargetMember = payRequestTargetRepository.findListByTargetMember(targetMemberJpaEntity);

        if (byTargetMember.isEmpty()) return new ArrayList<>();

        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (PayRequestTargetJpaEntity payRequestTargetJpaEntity : byTargetMember.get()) {
            payRequestTargets.add(payRequestTargetMapper.toDomainEntity(payRequestTargetJpaEntity));
        }

        return payRequestTargets;
    }

    @Override
    public List<PayRequestTarget> loadByPayRequestId(Long payRequestId) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findById(payRequestId).orElseThrow(
                () -> new CustomException(PAY_REQUEST_NOT_FOUND));

        Optional<List<PayRequestTargetJpaEntity>> byPayRequest = payRequestTargetRepository.findByPayRequest(payRequestJpaEntity);

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
        PayRequestTargetJpaEntity jpaEntity = payRequestTargetRepository.findById(payRequestTargetId).orElseThrow(
                () -> new CustomException(PAY_REQUEST_TARGET_NOT_FOUND));

        return payRequestTargetMapper.toDomainEntity(jpaEntity);
    }

    @Override
    public void update(PayRequestTarget payRequestTarget) {
        PayRequestTargetJpaEntity jpaEntity = payRequestTargetRepository.findById(payRequestTarget.getId()).orElseThrow(
                () -> new CustomException(PAY_REQUEST_TARGET_NOT_FOUND));

        jpaEntity.changeCompletionStatus(payRequestTarget.isComplete());
    }
}
