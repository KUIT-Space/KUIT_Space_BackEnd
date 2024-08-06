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
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final SpaceUtils spaceUtils;
    private final UserSpaceUtils userSpaceUtils;
    private final PostDao postDao;
    private final UserSpaceDao userSpaceDao;

    public List<ReadPostsResponse> getAllPosts(Long spaceId, String filter) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: 필터에 따라 해당 user의 해당 space 내의 게시판 게시글 리스트 return
        List<Post> posts;
        if("notice".equalsIgnoreCase(filter)) {
            posts = postDao.findBySpaceAndType(spaceBySpaceId, "notice");
        } else if ("general".equalsIgnoreCase(filter)){
            posts = postDao.findBySpaceAndType(spaceBySpaceId, "general");
        } else {
            // filter = "all" 일 경우 전체 게시글 조회
            posts = postDao.findBySpace(spaceBySpaceId);
        }

        int postCount = posts.size();

        return posts.stream()
                .map(post ->{
                    Optional<UserSpace> userSpace = userSpaceUtils.isUserInSpace(post.getUser().getUserId(), spaceId);
                    return ReadPostsResponse.of(post, postCount, userSpace.orElse(null));
                })
                .collect(Collectors.toList());
    }
}
