package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;

import java.util.List;

public interface CreatePayPort {

    void savePay(PayRequest payRequest, List<PayRequestTarget> payRequestTargets);
}
