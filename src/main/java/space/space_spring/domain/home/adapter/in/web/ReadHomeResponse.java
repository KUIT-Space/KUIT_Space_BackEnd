package space.space_spring.domain.home.adapter.in.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadHomeResponse {

    private String spaceName;

    private int memberCnt;

    private String img;

    private List<NoticeSummary> notices;

    private List<SubscriptionSummary> subscriptions;

    public ReadHomeResponse(ReadHomeResult readHomeResult) {
        this.spaceName = readHomeResult.spaceName;
        this.memberCnt = readHomeResult.memberCnt;
        this.img = readHomeResult.img;
        this.notices = readHomeResult.notices;
        this.subscriptions = readHomeResult.subscriptions;
    }
}
