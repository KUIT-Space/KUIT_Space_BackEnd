package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostBasePort;
import space.space_spring.domain.post.domain.PostBase;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_MEMBER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostBasePersistenceAdapter implements CreatePostBasePort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final PostBaseMapper postBaseMapper;

    @Override
    public Long createPostBase(PostBase postBase) {
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(postBase.getSpaceMemberId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        BoardJpaEntity boardJpaEntity = boardRepository.findById(postBase.getBoardId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        PostBaseJpaEntity postBaseJpaEntity = postBaseMapper.toJpaEntity(spaceMemberJpaEntity, boardJpaEntity, postBase);

        return postBaseRepository.save(postBaseJpaEntity).getId();
    }
}
