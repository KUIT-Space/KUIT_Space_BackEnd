package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.discord.MessageInputFromDiscordCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessagePort;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.global.exception.CustomException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class MoveCurrentChannelMessageButtonProcessor implements ButtonInteractionProcessor {

    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;
    private final LoadPostPort loadPostPort;
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("move-current-message")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();


        Long latestMsgId = event.getChannel().getLatestMessageIdLong();
        MessageHistory history =event.getChannel().getHistoryBefore(latestMsgId,100).complete();
        List<Message> messageHistory = history.getRetrievedHistory();


        //message 순서 뒤집기
        List<Message> messages = IntStream.range(0, messageHistory.size())
                .mapToObj(i -> messageHistory.get(messageHistory.size() - 1 - i))
                .collect(Collectors.toUnmodifiableList());

        Long boardId=loadBoardCacheUseCase.findByDiscordId(event.getChannelIdLong())
                .orElseThrow(()->{
                    event.reply(BOARD_NOT_FOUND.getMessage()).queue();
                    throw new CustomException(BOARD_NOT_FOUND);
                });

        event.deferReply().setEphemeral(true).queue();

        // 각 메시지에 대해 비동기 작업 수행
        List<CompletableFuture<Void>> futures = messages.stream()
                .filter(message->loadPostPort.loadByDiscordId(message.getIdLong()).isEmpty())
                .map(message -> {
                    return CompletableFuture.runAsync(()->{
                            inputMessageFromDiscordUseCase.putPost(mapToServer(message, boardId));
                    });
                })
                .collect(Collectors.toList());

        // 모든 메시지 처리가 완료될 때까지 기다림
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // 최종 완료 시 추가 작업 수행 (옵션)
        allFutures.thenRun(() -> {
            event.getHook().editOriginal("모든 메시지 처리가 완료되었습니다.").queue();
        }).exceptionally(ex -> {
            event.getHook().editOriginal("처리 중 예외 발생: " + ex.getMessage()).queue();
            return null;
        });
//        createDiscordWebHookMessagePort.send(mapToDiscord(message, webHookUrl))
//                .thenAccept(messageId->inputMessageFromDiscordUseCase.put(
//                        mapToServer( message,messageId)
//                    )
//                );

    }

    private record TitleContent(String title, String content) {}

    private TitleContent parseTitleAndContent(String input) {
        if (input == null || input.isBlank()) {
            return new TitleContent("", "");
        }

        String[] lines = input.split("\n", -1);
        String title = "";
        int index = 0;

        while (index < lines.length && lines[index].isBlank()) {
            index++;
        }

        if (index < lines.length) {
            title = lines[index];
            index++;
        }

        String content = String.join("\n", Arrays.copyOfRange(lines, index, lines.length));

        return new TitleContent(title, content);
    }

    private MessageInputFromDiscordCommand mapToServer(Message message, Long boardId){
        TitleContent titleContent = parseTitleAndContent(message.getContentRaw());

        //Todo 생성 시간 주입
        message.getTimeCreated();

        return MessageInputFromDiscordCommand.builder()
                .boardId(boardId)
                .MessageDiscordId(message.getIdLong())
                .isComment(false)
                .creatorDiscordId(message.getMember().getIdLong())
                .spaceDiscordId(message.getGuildIdLong())
                .title(titleContent.title())
                .content(titleContent.content())
                .build();
    }
}
