package space.space_spring.domain.pay.application.port.in.validatePayTarget;

public interface ValidatePayTargetUseCase {
    boolean hasPayTarget(Long SpaceMemberId,Long TargetRequestId);
}
