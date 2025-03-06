package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.adapter.out.persistence.jpaEntity.PayRequestJpaEntity;
import space.space_spring.domain.pay.adapter.out.persistence.mapper.PayRequestMapper;
import space.space_spring.domain.pay.application.port.out.CreatePayRequestPort;
import space.space_spring.domain.pay.application.port.out.DeletePayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestInfoPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_REQUEST_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Repository
public class PayRequestPersistenceAdapter implements CreatePayRequestPort, LoadPayRequestPort, LoadPayRequestInfoPort, DeletePayRequestPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final PayRequestMapper payRequestMapper;

    /**
     * 저장 성공한 PayRequestJpaEntity의 PK값 return
     */
    @Override
    public PayRequest createPayRequest(PayRequest payRequest) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findByIdAndStatus(payRequest.getPayCreatorId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));
        PayRequestJpaEntity save = payRequestRepository.save(payRequestMapper.toJpaEntity(payCreatorJpaEntity, payRequest));

        return payRequestMapper.toDomainEntity(save);
    }

    @Override
    public List<PayRequest> loadByPayCreatorId(Long payCreatorId) {
        SpaceMemberJpaEntity payCreatorJpaEntity = spaceMemberRepository.findByIdAndStatus(payCreatorId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        Optional<List<PayRequestJpaEntity>> byPayCreator = payRequestRepository.findListByPayCreatorAndStatus(payCreatorJpaEntity, BaseStatusType.ACTIVE);

        if (byPayCreator.isEmpty()) return new ArrayList<>();

        List<PayRequest> payRequests = new ArrayList<>();
        for (PayRequestJpaEntity payRequestJpaEntity : byPayCreator.get()) {
            payRequests.add(payRequestMapper.toDomainEntity(payRequestJpaEntity));
        }

        return Collections.unmodifiableList(payRequests);
    }

    @Override
    public PayRequest loadById(Long id) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findByIdAndStatus(id, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));
        return payRequestMapper.toDomainEntity(payRequestJpaEntity);
    }

    @Override
    public Bank loadBankOfPayRequestById(Long payRequestId) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findByIdAndStatus(payRequestId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));
        return Bank.of(payRequestJpaEntity.getBankName(), payRequestJpaEntity.getBankAccountNum());
    }

    @Override
    public void deletePayRequest(Long payRequestId) {
        PayRequestJpaEntity payRequestJpaEntity = payRequestRepository.findByIdAndStatus(payRequestId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(PAY_REQUEST_NOT_FOUND));
        payRequestJpaEntity.updateToInactive();
    }
}
