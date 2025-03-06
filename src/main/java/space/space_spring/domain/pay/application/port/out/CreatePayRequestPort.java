package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequest;

public interface CreatePayRequestPort {

    PayRequest createPayRequest(PayRequest payRequest);
}
