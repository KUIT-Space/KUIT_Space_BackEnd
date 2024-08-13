package space.space_spring.dto.chat.request;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

@Getter
public class JoinChatRoomRequest {

    @Nullable
    private List<Long> memberList;
}
