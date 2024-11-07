package space.space_spring.domain.chat.chatroom.model.response;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.dto.userSpace.UserInfoInSpace;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ReadChatRoomMemberResponse {

    private List<UserInfoInSpace> userList = new ArrayList<>();

    public static ReadChatRoomMemberResponse of(List<UserInfoInSpace> userList) {
        return ReadChatRoomMemberResponse.builder()
                .userList(userList)
                .build();
    }

}
