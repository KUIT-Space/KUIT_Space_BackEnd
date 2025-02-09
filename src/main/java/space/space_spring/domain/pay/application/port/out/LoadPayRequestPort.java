package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.SpaceMember;

import java.util.List;

public interface LoadPayRequestPort {

    List<PayRequest> loadByPayCreatorId(Long payCreatorId);
}
