package space.space_spring.domain.discord.application.port.in.createComment;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentCommand;

@Getter
@Builder
public class CreateCommentInDiscordCommand {
    private Long spaceId;
    private Long boardId;
    private Long originPostId;
    //익명일 경우 여기에 디스코드에 보여질 값을 입력
    private String userName;
    private String profileUrl;

    private String content;

    //private final List<Tag> tags; //not yet

    public static CreateCommentInDiscordCommand of(CreateCommentCommand command, String creatorNickname, String creatorProfileImageUrl) {
        return CreateCommentInDiscordCommand.builder()
                .spaceId(command.getSpaceId())
                .boardId(command.getBoardId())
                .originPostId(command.getPostId())
                .userName(creatorNickname)
                .profileUrl(creatorProfileImageUrl)
                .content(command.getContent())
                .build();
    }

}
