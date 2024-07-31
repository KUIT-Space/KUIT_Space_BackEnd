package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.BoardDao;
import space.space_spring.dao.UserDao;
import space.space_spring.dto.post.ReadBoardResponse;
import space.space_spring.entity.Board;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final SpaceUtils spaceUtils;
    private final BoardDao boardDao;

    public List<ReadBoardResponse> getAllPosts(Long spaceId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: 해당 user의 해당 space 내의 게시판 게시글 리스트 return
        List<Board> posts = boardDao.findBySpace(spaceBySpaceId);
        return posts.stream()
                .map(this::convertToReadResponse)
                .collect(Collectors.toList());
    }

    public ReadBoardResponse convertToReadResponse(Board board) {
        return ReadBoardResponse.builder()
                .postId(board.getPostId())
                .spaceId(board.getSpace().getSpaceId())
                .title(board.getTitle())
                .content(board.getContent())
                .type(board.getType())
                .like(board.getLike())
                .build();

    }


}
