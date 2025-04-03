package space.space_spring.domain.home.application.service;

import static space.space_spring.domain.post.domain.BoardType.NOTICE;
import static space.space_spring.domain.post.domain.BoardType.SEASON_NOTICE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.home.adapter.in.web.NoticeSummary;
import space.space_spring.domain.home.adapter.in.web.ReadHomeResult;
import space.space_spring.domain.home.adapter.in.web.SubscriptionSummary;
import space.space_spring.domain.home.application.port.in.ReadHomeUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.application.port.out.LoadSubscriptionPort;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.post.domain.Subscription;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.post.ConvertCreatedDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadHomeService implements ReadHomeUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpacePort loadSpacePort;
    private final LoadBoardPort loadBoardPort;
    private final LoadTagPort loadTagPort;
    private final LoadPostPort loadPostPort;
    private final LoadSubscriptionPort loadSubscriptionPort;

    @Override
    public ReadHomeResult readHome(Long spaceMemberId, Long spaceId) {
        ReadHomeResult result = new ReadHomeResult();

        Space space = loadSpacePort.loadSpaceById(spaceId)
                .orElseThrow(() -> new CustomException(SPACE_NOT_FOUND));
        List<SpaceMember> spaceMembers = loadSpaceMemberPort.loadSpaceMemberBySpaceId(spaceId);
        result.addMetaData(space);
        result.addMemberCnt(spaceMembers.size());

        // 전체 공지 + n기 공지 가져오기
        List<Long> noticeBoardIds = new ArrayList<>();
        loadBoardPort.loadByType(NOTICE).forEach(board -> noticeBoardIds.add(board.getId()));
        loadBoardPort.loadByType(SEASON_NOTICE).forEach(board -> noticeBoardIds.add(board.getId()));

        List<NoticeSummary> notices = new ArrayList<>();
        if (!noticeBoardIds.isEmpty()) {
            // 최신순 3개의 공지사항만 가져오기
            List<Post> noticePosts = loadPostPort.loadLatestPostsByBoardIds(noticeBoardIds, 3);

            for (Post post : noticePosts) {
                notices.add(new NoticeSummary(
                        post.getId(),
                        post.getTitle(),
                        ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getCreatedAt())
                ));
            }
        }
        result.addNotice(notices);

        // 구독한 게시판 가져오기
        List<SubscriptionSummary> subscriptions = new ArrayList<>();
        List<Subscription> subscribedBoards = loadSubscriptionPort.loadBySpaceMember(spaceMemberId);
        for (Subscription subscription : subscribedBoards) {
            Optional<Post> latestPost;
            String postTitle = "";
            String tagName = "";

            Board board = loadBoardPort.loadById(subscription.getBoardId());
            Long tagId = subscription.getTagId();

            if (tagId != null) {
                Tag tag = loadTagPort.loadById(subscription.getTagId());
                tagName = tag.getTagName();
                latestPost = loadPostPort.loadLatestPostByBoardIdAndTagId(board.getId(), tag.getId());
                if (latestPost.isPresent()) postTitle = latestPost.get().getTitle();
            } else {
                latestPost = loadPostPort.loadLatestPostsByBoardIds(List.of(board.getId()), 1)
                        .stream()
                        .findFirst();
                if (latestPost.isPresent()) postTitle = latestPost.get().getTitle();
            }

            subscriptions.add(new SubscriptionSummary(board.getId(), board.getBoardName(), postTitle, tagName));
        }
        result.addSubscription(subscriptions);

        return result;
    }
}
