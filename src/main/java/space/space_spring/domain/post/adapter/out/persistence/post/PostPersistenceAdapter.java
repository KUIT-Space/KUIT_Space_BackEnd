package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements CreatePostPort, LoadPostPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final PostMapper postMapper;
    private final PostBaseMapper postBaseMapper;

    @Override
    public Long createPost (Post post) {
        // 1. SpaceMember 조회
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(post.getSpaceMemberId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        // 2. Board 조회
        BoardJpaEntity boardJpaEntity = boardRepository.findById(post.getBoardId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // 3. PostBaseJpaEntity 생성 및 저장
        PostBaseJpaEntity postBaseJpaEntity = postBaseMapper.toJpaEntity(spaceMemberJpaEntity, boardJpaEntity, post);
        PostBaseJpaEntity savedPostBase = postBaseRepository.save(postBaseJpaEntity);

        // 4. PostJpaEntity 생성 및 저장
        PostJpaEntity postJpaEntity = postMapper.toJpaEntity(savedPostBase, post);

        return postRepository.save(postJpaEntity).getId();
    }

    @Override
    public List<Post> loadPostList(Long boardId) {
        // 1. 게시글 리스트 조회
        List<PostJpaEntity> postJpaEntities = postRepository.findPostsByBoardId(boardId, BaseStatusType.ACTIVE);
        return postJpaEntities.stream().map(postMapper::toDomainEntity).toList();
    }

    @Override
    public Post loadById(Long postId) {
        // Post 에 해당하는 jpa entity 찾기
        PostJpaEntity postJpaEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (!postJpaEntity.getPostBase().isActive()) {
            throw new CustomException(POST_NOT_FOUND);          // 찾은 Post가 Active 상태가 아닌 경우
        }

        return postMapper.toDomainEntity(postJpaEntity);
    }
}
