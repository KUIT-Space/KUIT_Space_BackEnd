package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.CreatePayUseCase;
import space.space_spring.domain.pay.application.port.out.SavePayPort;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {

    private final SavePayPort savePayPort;

    @Override
    @Transactional
    public void createPay(CreatePayCommand command) {

    }
}
