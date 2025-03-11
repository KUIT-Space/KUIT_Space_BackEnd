package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardInfoCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadBoardListService implements ReadBoardListUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public ReadBoardListCommand readBoardList(Long spaceMemberId, Long spaceId) {
        // 1. 게시판 조회
        List<Board> boardList = loadBoardPort.loadBySpaceId(spaceId);

        if (boardList.isEmpty()) {
            return ReadBoardListCommand.of(Collections.emptyList());
        }

        // 2. TODO: 사용자가 구독한 게시판 정보 추가


    }
}
