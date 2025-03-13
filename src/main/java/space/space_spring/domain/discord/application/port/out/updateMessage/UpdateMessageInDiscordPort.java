package space.space_spring.domain.discord.application.port.out.updateMessage;

import java.util.List;

public interface UpdateMessageInDiscordPort {
    void editMessage(String webHookUrl , Long boardDiscordId,Long messageDiscordId, String title,String content, List<Long> tags);
}
