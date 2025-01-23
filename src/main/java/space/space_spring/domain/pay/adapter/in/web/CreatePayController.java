package space.space_spring.domain.pay.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.CreatePayUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;

@RestController
@RequiredArgsConstructor
public class CreatePayController {

    private final CreatePayUseCase createPayUseCase;

    @PostMapping("/pay/create")
    public String createPay(@JwtLoginAuth Long id, @RequestBody RequestOfCreatePay request) {

        CreatePayCommand createPayCommand = CreatePayCommand.create(id, request);
        createPayUseCase.createPay(createPayCommand);
    }
}
