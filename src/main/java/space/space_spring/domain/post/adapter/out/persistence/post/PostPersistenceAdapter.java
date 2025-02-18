package space.space_spring.domain.post.adapter.out.persistence.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.attachment.AttachmentJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.attachment.SpringDataAttachmentRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.application.port.in.readPostList.SummaryOfPost;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements CreatePostPort, LoadPostPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final SpringDataAttachmentRepository attachmentRepository;
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
        PostBaseJpaEntity postBaseJpaEntity = postBaseMapper.toJpaEntity(post.getDiscordId(), spaceMemberJpaEntity, boardJpaEntity, post.getContent());
        PostBaseJpaEntity savedPostBase = postBaseRepository.save(postBaseJpaEntity);

        // 4. PostJpaEntity 생성 및 저장
        PostJpaEntity postJpaEntity = postMapper.toJpaEntity(savedPostBase, post);

        return postRepository.save(postJpaEntity).getId();
    }

    @Override
    public List<SummaryOfPost> loadPostList(Long boardId) {
        // 1. Board 조회
        BoardJpaEntity boardJpaEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // 2. PostBase 조회
        List<PostBaseJpaEntity> postBaseJpaEntities = postBaseRepository.findByBoard(boardJpaEntity);

        if (postBaseJpaEntities.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. Post 조회
        List<PostJpaEntity> postJpaEntities = postRepository.findByPostBaseIn(postBaseJpaEntities);

        // 4. Attachment 조회
        List<AttachmentJpaEntity> attachmentJpaEntities = attachmentRepository.findByPostBaseIn(postBaseJpaEntities);

        // 5. 각 Entity를 Domain 객체로 변환 후 리스트 조합
        return
    }

    private Post mapToPost(PostJpaEntity postJpaEntity, List<PostBaseJpaEntity> postBaseJpaEntities, List<AttachmentJpaEntity> attachmentJpaEntities) {
        // 1. PostBase 조회
        PostBaseJpaEntity postBaseJpaEntity = postBaseJpaEntities.stream()
                .filter(base -> base.getId().equals(postJpaEntity.getPostBase().getId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        // 첫 번째 이미지 첨부파일 가져오기
        String postImageUrl = attachmentJpaEntities.stream()
                .filter(attachment -> attachment.getPostBase().equals(postBaseJpaEntity) && attachment.getAttachmentType().equals(AttachmentType.IMAGE))
                .map(attachmentMapper::toDomain)
                .findFirst()
                .map(attachment -> attachment.getAttachmentUrl())
                .orElse(null);
        return postMapper.toDomain(postJpaEntity, postBaseJpaEntity, postImageUrl);
    }

}
