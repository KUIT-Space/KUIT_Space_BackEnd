package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.PostDao;
import space.space_spring.dao.PostImageDao;
import space.space_spring.dto.post.ReadPostsResponse;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostImage;
import space.space_spring.entity.Space;
import space.space_spring.util.space.SpaceUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final SpaceUtils spaceUtils;
    private final PostDao postDao;

    public List<ReadPostsResponse> getAllPosts(Long spaceId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: 해당 user의 해당 space 내의 게시판 게시글 리스트 return
        List<Post> posts = postDao.findBySpace(spaceBySpaceId);
        return posts.stream()
                .map(this::convertToReadResponse)
                .collect(Collectors.toList());
    }

    public ReadPostsResponse convertToReadResponse(Post post) {
        List<PostImage> postImages = post.getPostImages();
        List<String> postImageUrls = postImages.stream()
                .map(PostImage::getPostImgUrl)
                .toList();

        return ReadPostsResponse.builder()
                .postId(post.getPostId())
                .spaceId(post.getSpace().getSpaceId())
                .title(post.getTitle())
                .content(post.getContent())
                .type(post.getType())
                .like_count(post.getLike())
                .postImage(postImageUrls)
                .build();

    }

}
