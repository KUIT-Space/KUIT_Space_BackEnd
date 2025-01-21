package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements CreatePayPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;

    @Override
    public void createPay() {

    }
}
