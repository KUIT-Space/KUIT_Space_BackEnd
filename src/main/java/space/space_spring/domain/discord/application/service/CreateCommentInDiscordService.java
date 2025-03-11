package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.createComment.CreateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createComment.CreateCommentInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageOnThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageOnThreadPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CreateCommentInDiscordService implements CreateCommentInDiscordUseCase {
    private final CreateDiscordMessageOnThreadPort createDiscordMessageOnThreadPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadSpacePort loadSpacePort;
    private final LoadPostPort loadPostPort;
    @Override
    public CompletableFuture<Long> send(CreateCommentInDiscordCommand command){

        return createDiscordMessageOnThreadPort.sendToThread(mapToDiscordOnThread(command));

    }

    private CreateDiscordMessageOnThreadCommand mapToDiscordOnThread(CreateCommentInDiscordCommand command){
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getOriginPostId());
        return CreateDiscordMessageOnThreadCommand.builder()
                .threadChannelDiscordId(command.getOriginPostId())
                .guildDiscordId(loadSpacePort.loadSpaceById(command.getSpaceId()).get().getDiscordId())
                .avatarUrl(command.getProfileUrl())
                .userName(command.getUserName())
                .webHookUrl(board.getWebhookUrl())
                .content(command.getContent())
                .originPostId(post.getDiscordId())
                .originChannelId(board.getDiscordId())
                .build();
    }

}
