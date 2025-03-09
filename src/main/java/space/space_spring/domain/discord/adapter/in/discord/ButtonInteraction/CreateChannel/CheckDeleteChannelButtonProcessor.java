package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonUtil;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CheckDeleteChannelButtonProcessor implements ButtonInteractionProcessor {
    private final LoadBoardUseCase loadBoardUseCase;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("check:delete-channel:")){
            return true;
        }
        return false;

    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 4) return;

        Long boardId = Long.parseLong(parts[3]);
        String channelIdStr = parts[2];
        String channelName = parts[4];
        Long channelId = Long.parseLong(channelIdStr);
        int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
        String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환
        BoardType boardType = loadBoardUseCase.getBoardTypeById(boardId).orElseThrow(()->{
            throw new CustomException(BOARD_NOT_FOUND);
        });

        event.reply("**"+channelName+"**"+
                    "\n"+boardType.getKrName()+
                    "\n이 채널을 **삭제**하시겠습니까?")
                .addActionRow(
                        Button.primary(returnString, " 예 "),
                        Button.primary("cancel", "아니오")
                ).queue();
    }

}
