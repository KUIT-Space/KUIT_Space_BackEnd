package space.space_spring.domain.space.application.port.in;

public interface CreateSpaceUseCase {
    Long createSpace(CreateSpaceCommand command);
}
