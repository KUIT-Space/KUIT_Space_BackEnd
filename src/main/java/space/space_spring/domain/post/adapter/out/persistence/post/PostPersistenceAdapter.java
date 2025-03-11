package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.port.out.DeletePostPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.application.port.out.UpdatePostPort;
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
public class PostPersistenceAdapter implements CreatePostPort, LoadPostPort, UpdatePostPort, DeletePostPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final PostMapper postMapper;
    private final PostBaseMapper postBaseMapper;

    @Override
    public Long createPost (Post post) {
        // 1. SpaceMember 조회
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(post.getPostCreatorId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        // 2. Board 조회
        BoardJpaEntity boardJpaEntity = boardRepository.findById(post.getBoardId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // 3. PostBaseJpaEntity 생성 및 저장
        PostBaseJpaEntity postBaseJpaEntity = postBaseMapper.toJpaEntity(spaceMemberJpaEntity, boardJpaEntity, post);
        PostBaseJpaEntity savedPostBase = postBaseRepository.save(postBaseJpaEntity);

        System.out.println("postBaseId:"+savedPostBase.getId());

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

    @Override
    public Optional<Post> loadByDiscordId(Long discordId){
        PostJpaEntity postJpaEntity = postRepository.findByDiscordId(discordId,BaseStatusType.ACTIVE);
        if(postJpaEntity==null){
            return Optional.empty();
        }
        return Optional.of(postMapper.toDomainEntity(postJpaEntity));
  }
    public void updatePost(Post post) {
        // Post에 해당하는 jpa entity 찾기
        PostJpaEntity postJpaEntity = postRepository.findById(post.getId())
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (!postJpaEntity.getPostBase().isActive()) {
            throw new CustomException(POST_NOT_FOUND);      // 찾은 Post가 Active 상태가 아닌 경우
        }

        postJpaEntity.getPostBase().changeContent(post.getContent().getValue());
        postJpaEntity.updatePost(post.getTitle(), postJpaEntity.getIsAnonymous());
    }

    @Override
    public void deletePost(Long postId) {
        // Post에 해당하는 jpa entity 찾기
        PostJpaEntity postJpaEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (!postJpaEntity.getPostBase().isActive()) {
            throw new CustomException(POST_NOT_FOUND);      // 찾은 Post가 Active 상태가 아닌 경우
        }

        // jpa entity를 INACTIVE 상태로 변경
        postJpaEntity.getPostBase().updateToInactive();
    }
}
