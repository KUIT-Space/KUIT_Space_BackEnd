package space.space_spring.domain.post.application.port.in.readBoardList;

public interface ReadBoardListUseCase {

    ReadBoardListCommand readBoardList(Long spaceMemberId, Long spaceId);

}
