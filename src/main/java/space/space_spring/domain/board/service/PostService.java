package space.space_spring.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import space.space_spring.domain.board.model.entity.Post;
import space.space_spring.domain.board.model.entity.PostImage;
import space.space_spring.domain.board.repository.CommentRepository;
import space.space_spring.domain.board.repository.PostRepository;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.board.model.response.ReadCommentsResponse;
import space.space_spring.domain.board.model.request.CreatePostRequest;
import space.space_spring.domain.board.model.response.ReadPostDetailResponse;
import space.space_spring.domain.board.model.response.ReadPostsResponse;
import space.space_spring.domain.space.model.dto.GetSpaceHomeDto;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.S3Uploader;
import space.space_spring.global.util.space.SpaceUtils;
import space.space_spring.global.util.user.UserUtils;
import space.space_spring.global.util.userSpace.UserSpaceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_IS_NOT_IN_SPACE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_NOT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final UserSpaceUtils userSpaceUtils;
    private final PostRepository postRepository;
    private final UserSpaceDao userSpaceDao;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public List<ReadPostsResponse> getAllPosts(Long spaceId, String filter, Long userId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: 필터에 따라 해당 user의 해당 space 내의 게시판 게시글 리스트 return
        List<Post> posts;
        if("notice".equalsIgnoreCase(filter)) {
            posts = postRepository.findBySpaceAndType(spaceBySpaceId, "notice");
        } else if ("general".equalsIgnoreCase(filter)){
            posts = postRepository.findBySpaceAndType(spaceBySpaceId, "general");
        } else {
            // filter = "all" 일 경우 전체 게시글 조회
            posts = postRepository.findBySpace(spaceBySpaceId);
        }

        int postCount = posts.size();

        return posts.stream()
                .map(post ->{
                    Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(post.getUser(), post.getSpace());
                    boolean isLike = postRepository.isUserLikedPost(post.getPostId(), userId);
                    return ReadPostsResponse.of(post, postCount, userSpace.orElse(null), isLike);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(Long userId, Long spaceId, CreatePostRequest createPostRequest) {
        // TODO 1: userId에 해당하는 User find
        User user = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space space = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 게시글 db insert
        List<PostImage> postImages = Optional.ofNullable(createPostRequest.getPostImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(file -> {
                    try {
                        log.info("Received files: {}", createPostRequest.getPostImages());
                        String postImgUrl = s3Uploader.upload(file, "postImg");
                        log.info("Post Image URL: {}", postImgUrl);
                        return PostImage.builder().postImgUrl(postImgUrl).build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file", e);
                    }
                }).collect(Collectors.toList());
        log.info("Uploading files: {}", postImages);

        // TODO 4: Post 객체를 생성하고, 생성된 Post 객체를 각 PostImage에 할당
        Post post = createPostRequest.toEntity(user, space, postImages);

        // TODO 5: 각 PostImage에 해당 Post를 설정
        postImages.forEach(postImage -> postImage.setPost(post));

        return postRepository.save(post).getPostId();
    }

    @Transactional
    public ReadPostDetailResponse getPostDetail(Long userId, Long spaceId, Long postId) {
        // TODO 1: spaceId에 해당하는 space find
        Space space = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: postId에 해당하는 post find
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // TODO 3: 게시글이 해당 스페이스에 속하는지 검증
        if (!post.getSpace().getSpaceId().equals(spaceId)) {
            throw new CustomException(POST_IS_NOT_IN_SPACE);
        }

        // TODO 4: userId와 spaceId에 해당하는 UserSpace find
        Optional<UserSpace> userSpace = userSpaceUtils.isUserInSpace(post.getUser().getUserId(), spaceId);

        // TODO 5: 유저가 해당 게시글에 좋아요를 눌렀는지 여부 확인
        boolean isLike = postRepository.isUserLikedPost(post.getPostId(), userId);

        // TODO 6: 댓글 리스트를 ReadCommentsResponse로 변환
        List<ReadCommentsResponse> comments = post.getComments().stream()
                .map(comment -> {
                    int commentCount = commentRepository.countByTargetId(comment.getCommentId());
                    boolean isCommentLiked = commentRepository.isUserLikedComment(comment.getCommentId(), userId);
                    Optional<UserSpace> userSpaceOpt = userSpaceDao.findUserSpaceByUserAndSpace(comment.getUser(), post.getSpace());

                    return ReadCommentsResponse.of(comment, userSpaceOpt.orElse(null), isCommentLiked, commentCount);
                })
                .collect(Collectors.toList());

        // TODO 7: ReadPostDetailResponse 객체로 변환
        return ReadPostDetailResponse.of(post, userSpace.orElse(null), isLike, comments);
    }

    public List<GetSpaceHomeDto.SpaceHomeNotice> getNoticeInfoForHome(Long spaceId) {

        // TODO 1. spaceId로 Space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2. Space에 해당하는 notice 게시글 get
        // 공지사항 중 3개만 return
        List<Post> noticeList = postRepository.findBySpaceAndTypeSortedByNewest(spaceBySpaceId, "notice", Pageable.ofSize(3));

        // TODO 3. return
        List<GetSpaceHomeDto.SpaceHomeNotice> spaceHomeNoticeList = new ArrayList<>();
        for (Post post : noticeList) {
            GetSpaceHomeDto.SpaceHomeNotice notice = new GetSpaceHomeDto.SpaceHomeNotice(post.getPostId(), post.getTitle());
            spaceHomeNoticeList.add(notice);
        }

        return spaceHomeNoticeList;
    }

    @Transactional
    public void updatePost(Long userId, Long spaceId, Long postId, CreatePostRequest updatePostReqeust) {
        // TODO 1: postId에 해당하는 post find
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // TODO 2: 게시글이 해당 스페이스에 속하는 지 검증
        if(!post.getSpace().getSpaceId().equals(spaceId)) {
            throw new CustomException(POST_IS_NOT_IN_SPACE);
        }

        // TODO 3: 기존 이미지 삭제 및 새로운 이미지 업로드 처리
        List<PostImage> updateImages = Optional.ofNullable(updatePostReqeust.getPostImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(file -> {
                    try {
                        String postImgUrl = s3Uploader.upload(file, "postImg");
                        return PostImage.builder().postImgUrl(postImgUrl).build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file", e);
                    }
                }).collect(Collectors.toList());

        post.updatePost(updatePostReqeust.getTitle(), updatePostReqeust.getContent(), updateImages);

        updateImages.forEach(image -> image.setPost(post));
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long userId, Long spaceId, Long postId) {
        // TODO 1: postId에 해당하는 post find
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // TODO 2: 게시글이 해당 스페이스에 속하는 지 검증
        if(!post.getSpace().getSpaceId().equals(spaceId)) {
            throw new CustomException(POST_IS_NOT_IN_SPACE);
        }

        // TODO 3: 게시글 삭제
        post.updateInactive();
        postRepository.save(post);

    }

}
