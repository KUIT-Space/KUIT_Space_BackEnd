package space.space_spring.domain.pay.application.port.in;

import lombok.Getter;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.pay.adapter.in.web.TargetOfPayRequest;

import java.util.List;

@Getter
public class CreatePayCommand {

    /**
     * 유스케이스 입력 모델 -> 유스케이스로의 유효성 검사 (필요하다면)
     */

    private Long payCreatorId;

    private Money totalAmount;

    private Bank bank;

    private List<TargetOfCreatePayDto> targets;

    private PayType payType;



}
