package space.space_spring.domain.home.application.service;

import static space.space_spring.domain.post.domain.BoardType.NOTICE;
import static space.space_spring.domain.post.domain.BoardType.SEASON_NOTICE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.NOTICE_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
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
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.post.domain.Subscription;
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
        List<Board> noticeBoard = loadBoardPort.loadByType(NOTICE);
        List<Board> seasonNoticeBoard = loadBoardPort.loadByType(SEASON_NOTICE);
        if (noticeBoard.isEmpty() || seasonNoticeBoard.isEmpty()) throw new CustomException(NOTICE_NOT_FOUND);

        List<Board> allNoticeBoard = new ArrayList<>();
        allNoticeBoard.addAll(noticeBoard);
        allNoticeBoard.addAll(seasonNoticeBoard);

        List<NoticeSummary> notices = new ArrayList<>();
        for (Board board : allNoticeBoard) {
            List<Post> posts = loadPostPort.loadPostListByBoardId(board.getId());

            for (Post post : posts) {
                if (notices.size() >= 3) break;
                notices.add(new NoticeSummary(post.getContent().getValue(), ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getCreatedAt())));
            }

            if (notices.size() >= 3) break;
        }
        result.addNotice(notices);

        // 구독한 게시판 가져오기
        List<SubscriptionSummary> subscriptions = new ArrayList<>();
        List<Subscription> subscribedBoards = loadSubscriptionPort.loadBySpaceMember(spaceMemberId);
        for (Subscription subscription : subscribedBoards) {
            Board board = loadBoardPort.loadById(subscription.getBoardId());
            List<Post> posts = loadPostPort.loadPostListByBoardId(board.getId());
            for (Post post : posts) {
                subscriptions.add(new SubscriptionSummary(board.getId(), board.getBoardName(), post.getTitle()));
            }
        }
        result.addSubscription(subscriptions);

        return result;
    }
}
