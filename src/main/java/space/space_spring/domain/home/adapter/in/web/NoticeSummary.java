package space.space_spring.domain.home.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSummary {

    private Long boardId;

    private Long postId;

    private String title;

    private String timePassed;
}
