package space.space_spring.domain.home.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionSummary {

    private Long boardId;

    private String boardName;

    private String postTitle;

    private String tagName;
}
