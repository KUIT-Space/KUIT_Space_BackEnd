package space.space_spring.domain.pay.application.port.in.readPayDetail;

import space.space_spring.domain.pay.application.port.in.readPayHome.ResultOfPayHomeView;

public interface ReadPayDetailUseCase {

    ResultOfReadPayDetail readPayDetail(Long spaceMemberId, Long payRequestId);
}
