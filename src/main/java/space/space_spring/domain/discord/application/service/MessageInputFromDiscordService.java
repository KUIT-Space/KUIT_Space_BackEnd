package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;

@Service
@RequiredArgsConstructor
public class MessageInputFromDiscordService implements InputMessageFromDiscordUseCase {

    private final CreatePostPort createPostPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpacePort loadSpacePort;
    @Override
    public void put(MessageInputFromDiscordCommand command){

        Long spaceMemberId = loadSpaceMemberPort.loadByDiscord(
                command.getSpaceDiscordId(),
                command.getCreatorDiscordId()).getId();

        Post post = Post.withoutId(
                command.getMessageDiscordId(),
                command.getBoardId(),
                spaceMemberId,
                getTitle(command.getRowContent()),
                Content.of(command.getRowContent())
                );
        createPostPort.createPost(post);
    }

    private String getTitle(String rowContent){
        return rowContent.split("\n")[0];
    }
}
