package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.PostDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.post.ReadPostsResponse;
import space.space_spring.entity.Post;
import space.space_spring.entity.Space;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.space.SpaceUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final SpaceUtils spaceUtils;
    private final PostDao postDao;
    private final UserSpaceDao userSpaceDao;

    public List<ReadPostsResponse> getAllPosts(Long spaceId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: 해당 user의 해당 space 내의 게시판 게시글 리스트 return
        List<Post> posts = postDao.findBySpace(spaceBySpaceId);
        int postCount = posts.size();

        return posts.stream()
                .map(post ->{
                    UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(post.getUser(), post.getSpace())
                            .orElseThrow(() -> new IllegalStateException("스페이스 안에 사용자가 없음"));
                    return ReadPostsResponse.of(post, postCount, userSpace);
                })
                .collect(Collectors.toList());
    }
}
