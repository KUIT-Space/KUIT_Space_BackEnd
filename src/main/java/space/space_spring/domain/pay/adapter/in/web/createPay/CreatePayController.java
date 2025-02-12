package space.space_spring.domain.pay.adapter.in.web.createPay;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
public class CreatePayController {

    private final CreatePayUseCase createPayUseCase;

    @PostMapping("/pay/create")
    public BaseResponse<Long> createPay(@JwtLoginAuth Long id, @Validated @RequestBody RequestOfCreatePay request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_PAY_CREATE, getErrorMessage(bindingResult));
        }

        CreatePayCommand createPayCommand = CreatePayCommand.builder()
                .payCreatorId(id)
                .totalAmount(request.getTotalAmount())
                .bankName(request.getBankName())
                .bankAccountNum(request.getBankAccountNum())
                .targets(request.getTargets())
                .valueOfPayType(request.getValueOfPayType())
                .build();

        return new BaseResponse<>(createPayUseCase.createPay(createPayCommand));
    }
}
