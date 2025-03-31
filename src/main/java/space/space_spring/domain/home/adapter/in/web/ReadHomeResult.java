package space.space_spring.domain.home.adapter.in.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.domain.Space;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadHomeResult {

    public String spaceName;

    public int memberCnt;

    public String img;

    public List<NoticeSummary> notices;

    public List<SubscriptionSummary> subscriptions;

    public void addMetaData(Space space) {
        this.spaceName = space.getName();
    }

    public void addMemberCnt(int memberCnt) {
        this.memberCnt = memberCnt;
    }

    public void addNotice(List<NoticeSummary> notices) {
        this.notices = notices;
    }

    public void addSubscription(List<SubscriptionSummary> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
