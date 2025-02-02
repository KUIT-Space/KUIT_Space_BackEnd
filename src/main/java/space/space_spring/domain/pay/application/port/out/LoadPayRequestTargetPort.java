package space.space_spring.domain.pay.application.port.out;

import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.SpaceMember;

import java.util.List;

public interface LoadPayRequestTargetPort {

    List<PayRequestTarget> loadListByTargetMember(SpaceMember targetMember);
}
