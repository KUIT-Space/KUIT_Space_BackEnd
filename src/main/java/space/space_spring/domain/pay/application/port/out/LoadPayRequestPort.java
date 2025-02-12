package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequest;

import java.util.List;

public interface LoadPayRequestPort {

    List<PayRequest> loadByPayCreatorId(Long payCreatorId);

    PayRequest loadById(Long id);
}
