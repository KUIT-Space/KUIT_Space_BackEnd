package space.space_spring.domain.chat.chatroom.model.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.entity.User;

public class UserChatRooms {

    private final List<UserChatRoom> userChatRooms;

    private UserChatRooms(List<UserChatRoom> userChatRooms) {
        this.userChatRooms = Collections.unmodifiableList(userChatRooms);
    }

    public static UserChatRooms of(List<UserChatRoom> userChatRooms) {
        return new UserChatRooms(userChatRooms);
    }

    public boolean isUserJoined(Long userId) {
        return userChatRooms.stream().anyMatch(userChatRoom -> userChatRoom.getUser().getUserId().equals(userId));
    }

    public List<User> getUsers() {
        return userChatRooms.stream()
                .map(UserChatRoom::getUser)
                .collect(Collectors.toUnmodifiableList());
    }

}
