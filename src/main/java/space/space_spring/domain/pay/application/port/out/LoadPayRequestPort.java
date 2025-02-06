package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

public interface LoadPayRequestPort {

    List<PayRequest> loadByPayCreator(SpaceMember payCreator);
}
