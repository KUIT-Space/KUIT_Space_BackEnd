package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;
import space.space_spring.domain.spaceMember.SpaceMember;

@Getter
public class Question {

    private Long id;

    private PostBase postBaseId;

    private SpaceMember spaceMemberId;

    private String title;

    private boolean isAnonymous;

    public Question(Long id, Long discordId, Board board, Text content, String title, boolean isAnonymous) {

        this.title = title;
        this.isAnonymous = isAnonymous;
    }


}
