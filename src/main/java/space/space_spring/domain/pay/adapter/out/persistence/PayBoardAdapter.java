package space.space_spring.domain.pay.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.pay.application.port.out.LoadPayBoardPort;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardPersistenceAdapter;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_BOARD_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PayBoardAdapter implements LoadPayBoardPort {
    private final LoadBoardPort loadBoardPort;

    @Override
    public Long getPayBoardDiscordId(Long spaceId){
        List<Board> boardList = loadBoardPort.loadByType(BoardType.PAY);
        if(boardList.isEmpty()){
            throw new CustomException(PAY_BOARD_NOT_FOUND);
        }
        if(boardList.size()>1){
            throw new CustomException(PAY_BOARD_NOT_FOUND,"두개 이상의 정산 게시판이 있습니다. 관리자에게 문의 해주세요");
        }
        return boardList.get(0).getDiscordId();
    }
}
