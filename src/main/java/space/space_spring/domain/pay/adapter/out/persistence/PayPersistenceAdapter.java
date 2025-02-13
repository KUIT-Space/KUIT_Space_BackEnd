package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.*;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements CreatePayPort, LoadPayRequestPort, LoadPayRequestTargetPort, LoadPayRequestInfoPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestMapper payRequestMapper;
    private final PayRequestTargetMapper payRequestTargetMapper;

    /**
     * 저장 성공한 PayRequestJpaEntity의 PK값 return
     */
    @Override
    public PayRequest createPayRequest(PayRequest payRequest) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findById(payRequest.getPayCreatorId()).orElseThrow(
                () -> new CustomException(SPACE_MEMBER_NOT_FOUND));
        PayRequestJpaEntity save = payRequestRepository.save(payRequestMapper.toJpaEntity(payCreatorJpaEntity, payRequest));

        return payRequestMapper.toDomainEntity(save);
    }

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
    public List<PayRequest> loadByPayCreatorId(Long payCreatorId) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findById(payCreatorId).orElseThrow(
                () -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        Optional<List<PayRequestJpaEntity>> byPayCreator = payRequestRepository.findListByPayCreator(payCreatorJpaEntity);

        if (byPayCreator.isEmpty()) return new ArrayList<>();

        List<PayRequest> payRequests = new ArrayList<>();
        for (PayRequestJpaEntity payRequestJpaEntity : byPayCreator.get()) {
            payRequests.add(payRequestMapper.toDomainEntity(payRequestJpaEntity));
        }

        return Collections.unmodifiableList(payRequests);
    }

    @Override
    public PayRequest loadById(Long id) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findById(id).orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));
        return payRequestMapper.toDomainEntity(payRequestJpaEntity);
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
    public Bank loadBankOfPayRequestById(Long payRequestId) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findById(payRequestId).orElseThrow(
                () -> new CustomException(PAY_REQUEST_NOT_FOUND));
        return Bank.of(payRequestJpaEntity.getBankName(), payRequestJpaEntity.getBankAccountNum());
    }
}
