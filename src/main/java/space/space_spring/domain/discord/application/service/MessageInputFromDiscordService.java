package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.common.entity.BaseInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageInputFromDiscordService implements InputMessageFromDiscordUseCase {

    private final CreatePostPort createPostPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpaceUseCase LoadSpaceUseCase;
    private final CreatePostUseCase createPostUseCase;
    @Override
    @Transactional
    public void put(MessageInputFromDiscordCommand command){
        if(!command.validateContentLength()){
            log.info("post message length less than 20 ");
            return;
        }
        Long spaceMemberId = loadSpaceMemberPort.loadByDiscord(
                command.getSpaceDiscordId(),
                command.getCreatorDiscordId()).getId();
        Long spaceId=LoadSpaceUseCase.loadByDiscordId(command.getSpaceDiscordId()).getId();
        //log.info("spaceMemberId:"+spaceMemberId);

        createPostUseCase.createPostFromDiscord(
                CreatePostCommand.builder()
                        .attachments(List.of())
                        .title(command.getTitle())
                        .spaceId(spaceId)
                        .content(command.getContentNotBlank())
                        .boardId(command.getBoardId())
                        .postCreatorId(spaceMemberId)
                        .isAnonymous(false)
                        .build()
                , command.getMessageDiscordId()
        );


    }

    private String getTitle(String rowContent){
        return rowContent.split("\n")[0];
    }
//    private void printPost(MessageInputFromDiscordCommand command){
//        System.out.println(command.toString());
//    }
}
