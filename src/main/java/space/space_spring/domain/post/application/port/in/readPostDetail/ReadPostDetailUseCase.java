package space.space_spring.domain.post.application.port.in.readPostDetail;

public interface ReadPostDetailUseCase {

    ResultOfReadPostDetail readPostDetail(ReadPostDetailCommand command);
}
