package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.SavePayPort;

@RequiredArgsConstructor
@Repository
public class PayPersistenceAdapter implements SavePayPort {

    private final SpringDataPayRequestRepository payRequestRepository;
    private final SpringDataPayRequestTargetRepository payRequestTargetRepository;

    @Override
    public void savePay() {

    }
}
