package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardInfoCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadBoardListService implements ReadBoardListUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadTagPort loadTagPort;
    private final LoadSubscriptionPort loadSubscriptionPort;

    @Override
    public ReadBoardListCommand readBoardList(Long spaceMemberId, Long spaceId) {
        // 1. 게시판 조회
        List<Board> boardList = loadBoardPort.loadBySpaceId(spaceId);

        List<Long> boardIds = boardList.stream()
                .map(Board::getId)
                .toList();

        // 2. 태그 조회
        List<Tag> tagList = loadTagPort.loadTagsByBoardIds(boardIds);

        // 3. 태그를 boardId 기준으로 매핑
        Map<Long, List<Tag>> tagMap = tagList.stream()
                .collect(Collectors.groupingBy(Tag::getBoardId));

        // 4. 사용자가 구독한 게시판 id 조회
        Set<Long> subscribedBoardIds = new HashSet<>(loadSubscriptionPort.loadSubscribedBoardIds(spaceMemberId));

        // 5. 게시판 목록 리스트 생성
        List<ReadBoardInfoCommand> boardInfoCommands = boardList.stream()
                .flatMap(board -> {
                    List<Tag> tags = tagMap.getOrDefault(board.getId(), Collections.emptyList());
                    return tags.stream()
                            .map(tag -> ReadBoardInfoCommand.of(
                                    board.getId(),
                                    board.getBoardName(),
                                    tag.getId(),
                                    tag.getTagName(),
                                    subscribedBoardIds.contains(board.getId())
                            ));
                }).toList();

        return ReadBoardListCommand.of(boardInfoCommands);
    }
}
