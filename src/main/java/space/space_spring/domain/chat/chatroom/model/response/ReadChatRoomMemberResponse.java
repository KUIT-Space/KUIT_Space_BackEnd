package space.space_spring.domain.chat.chatroom.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.dto.userSpace.UserInfoInSpace;

import java.util.List;

@Getter
public class ReadChatRoomMemberResponse {

    private List<UserInfoInSpace> userList;

    @Builder(access = AccessLevel.PRIVATE)
    private ReadChatRoomMemberResponse(List<UserInfoInSpace> userList) {
        this.userList = userList;
    }

    public static ReadChatRoomMemberResponse of(List<UserInfoInSpace> userList) {
        return ReadChatRoomMemberResponse.builder()
                .userList(userList)
                .build();
    }

}
