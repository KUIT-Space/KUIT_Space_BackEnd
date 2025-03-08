package space.space_spring.domain.pay.adapter.in.web.createPay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pay", description = "정산 관련 API")
public class CreatePayController {

    private final CreatePayUseCase createPayUseCase;

    @Operation(summary = "정산 요청 생성", description = """
            
            스페이스 멤버가 같은 스페이스에 속한 멤버들에게 정산 요청을 생성합니다.
            
            """)
    @PostMapping("/space/{spaceId}/pay/create")
    public BaseResponse<ResponseOfCreatePay> createPay(@JwtLoginAuth Long spaceMemberId, @Validated @RequestBody RequestOfCreatePay request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_PAY_CREATE, getErrorMessage(bindingResult));
        }

        CreatePayCommand createPayCommand = CreatePayCommand.builder()
                .payCreatorId(spaceMemberId)
                .totalAmount(request.getTotalAmount())
                .bankName(request.getBankName())
                .bankAccountNum(request.getBankAccountNum())
                .targets(request.getTargets())
                .valueOfPayType(request.getValueOfPayType())
                .build();

        return new BaseResponse<>(ResponseOfCreatePay.of(createPayUseCase.createPay(createPayCommand)));
    }
}
