package space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState;

public interface LoadCurrentPayRequestStateUseCase {

    CurrentPayRequestState loadCurrentPayRequestState(Long payRequestId);
}
