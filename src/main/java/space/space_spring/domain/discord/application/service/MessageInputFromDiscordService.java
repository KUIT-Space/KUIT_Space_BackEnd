package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.discord.CommentInputFromDiscordCommand;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.Tag.LoadTagUseCase;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.service.CreateCommentService;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.global.common.entity.BaseInfo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageInputFromDiscordService implements InputMessageFromDiscordUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpaceUseCase loadSpaceUseCase;
    private final CreateCommentService createCommentService;
    private final CreatePostUseCase createPostUseCase;
    private final LoadTagUseCase loadTagUseCase;

    @Override
    @Transactional
    public void putPost(MessageInputFromDiscordCommand command){
        if(!command.validateContentLength()){
            log.info("post message length less than 20 ");
            return;
        }

        Long spaceMemberId = loadSpaceMemberPort.loadByDiscord(
                command.getSpaceDiscordId(),
                command.getCreatorDiscordId()).getId();
        Long spaceId=loadSpaceUseCase.loadByDiscordId(command.getSpaceDiscordId()).getId();
        //log.info("spaceMemberId:"+spaceMemberId);
        List<Long> tagIds = new ArrayList();
        if(!command.getTagDiscordIds().isEmpty()){
            tagIds.addAll(loadTagUseCase.findByDiscordId(command.getTagDiscordIds()).stream().map(
                    tag-> tag.getId()
            ).toList());
        }

        createPostUseCase.createPostFromDiscord(
                CreatePostFromDiscordCommand.builder()
                        .attachments(command.getAttachments())
                        .title(command.getTitle())
                        .spaceId(spaceId)
                        .content(command.getContentNotBlank())
                        .boardId(command.getBoardId())
                        .postCreatorId(spaceMemberId)
                        .isAnonymous(false)
                        .tagIds(tagIds)
                        .build()
                , command.getMessageDiscordId()
        );


    }

    private String getTitle(String rowContent){
        return rowContent.split("\n")[0];
    }
    private void printPost(MessageInputFromDiscordCommand command){
        System.out.println(command.toString());
    }
    @Override
    public void putComment(CommentInputFromDiscordCommand command, Long boardId){

        createCommentService.createCommentFromDiscord(mapToCreateComment(command,boardId),command.getCreatorDiscordId());

    }

    private CreateCommentCommand mapToCreateComment(CommentInputFromDiscordCommand command,Long boardId){
        Long creatorId = loadSpaceMemberPort.loadByDiscord(
                command.getSpaceDiscordId(),
                command.getCreatorDiscordId()).getId();
        Long spaceId=loadSpaceUseCase.loadByDiscordId(command.getSpaceDiscordId()).getId();
        return CreateCommentCommand.builder()
                .commentCreatorId(creatorId)
                .isAnonymous(false)
                .spaceId(spaceId)
                .boardId(command.getBoardId())
                .content(command.getContent())
                .postId(boardId)
                .build();
    }


//    private void printPost(MessageInputFromDiscordCommand command){
//        System.out.println(command.toString());
//    }
}
