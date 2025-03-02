package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.ExecutionException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_EXIST;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_THREAD_CREATE_FAIL;

@Service
@RequiredArgsConstructor
public class CreatePostInDiscordService implements CreatePostInDiscordUseCase {
    private final LoadSpacePort loadSpacePort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberinfoPort;
    private final CreateDiscordThreadPort createDiscordThreadPort;
    private final LoadBoardPort loadBoardPort;
    public Long CreateMessageInDiscord(CreatePostInDiscordCommand command){
        try{
            return createDiscordThreadPort.create(MapToDiscordCommand(command))
                .get();
        }catch (InterruptedException e) {
            // 현재 스레드의 인터럽트 상태를 재설정합니다.
            Thread.currentThread().interrupt();
            // 필요한 경우 사용자 정의 예외로 감싸서 throw 합니다.
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL ,"InterruptedException 발생"+e.toString());
        }catch (ExecutionException e) {
            // ExecutionException 내부의 실제 원인을 추출하여 throw 합니다.
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL ,"ExecutionException 발생"+ e.toString());
        }
    }

    private CreateDiscordThreadCommand MapToDiscordCommand(CreatePostInDiscordCommand command){
        NicknameAndProfileImage userInfo = loadSpaceMemberinfoPort.loadNicknameAndProfileImageById(command.getPostCreatorId());
        Long guildDiscordId = loadSpacePort.loadSpaceById(command.getSpaceId()).get().getDiscordId();
        Board board=loadBoardPort.load(command.getBoardId()).orElseThrow(()->new CustomException(BOARD_NOT_EXIST));
        return CreateDiscordThreadCommand.builder()
                .channelDiscordId(board.getDiscordId())
                .guildDiscordId(guildDiscordId)
                .userName(userInfo.getNickname())
                .avatarUrl(userInfo.getProfileImageUrl())
                .webHookUrl(board.getWebhookUrl())
                .startMessage(command.getTitle())
                .threadName(command.getTitle())
                .contentMessage(command.getContent().getValue())
                .build();
    }


}
