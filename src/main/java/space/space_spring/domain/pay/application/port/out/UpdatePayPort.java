package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequestTarget;

public interface UpdatePayPort {

    void update(PayRequestTarget payRequestTarget);
}
