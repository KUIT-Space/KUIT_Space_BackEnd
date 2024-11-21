package space.space_spring.domain.chat.chatroom.service.component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.domain.chat.chatroom.model.dto.ChatRooms;
import space.space_spring.domain.chat.chatroom.model.dto.UserChatRooms;
import space.space_spring.domain.chat.chatroom.model.response.ChatRoomResponse;
import space.space_spring.domain.chat.chatroom.model.response.ChatSuccessResponse;
import space.space_spring.domain.chat.chatroom.model.response.CreateChatRoomResponse;
import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomMemberResponse;
import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomResponse;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.domain.chat.chatroom.model.dto.LastMessageInfoDto;
import space.space_spring.domain.chat.chatroom.model.request.JoinChatRoomRequest;
import space.space_spring.domain.chat.chatroom.model.request.CreateChatRoomRequest;
import space.space_spring.domain.chat.chatroom.service.module.ChatRoomModuleService;
import space.space_spring.domain.chat.chatroom.service.module.UserChatRoomModuleService;
import space.space_spring.domain.chat.chatting.service.module.ChattingModuleService;
import space.space_spring.dto.userSpace.UserInfoInSpace;
import space.space_spring.entity.*;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;

import space.space_spring.entity.enumStatus.BaseStatusType;
import space.space_spring.exception.CustomException;
import space.space_spring.global.util.TimeUtils;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.util.*;
import space.space_spring.util.userSpace.UserSpaceUtils;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserUtils userUtils;
    // findUserByUserId -> common module service
    private final SpaceUtils spaceUtils;
    // findSpaceBySpaceId -> common module service
    private final UserSpaceUtils userSpaceUtils;
    // findUserSpaceByUserAndSpace -> common module service

    private final ChatRoomModuleService chatRoomModuleService;
    private final ChattingModuleService chattingModuleService;
    private final UserChatRoomModuleService userChatRoomModuleService;
    private final TimeUtils timeUtils;

    @Transactional
    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        ChatRooms chatRooms = ChatRooms.of(chatRoomModuleService.findChatRooms(userId, spaceId));

        List<ChatRoomResponse> chatRoomResponses = chatRooms.toChatRoomResponses(
                userId,
                this::getLastMsgInfo,
                (Long, chatRoom) -> calculateUnreadMsgCount(userId, chatRoom.getId(),
                        getLastMsgInfo(chatRoom).getLastUpdateTime())
        );

        return ReadChatRoomResponse.of(chatRoomResponses);
    }

    @Transactional
    public CreateChatRoomResponse createChatRoom(Long userId, Long spaceId, CreateChatRoomRequest createChatRoomRequest,
                                                 String chatRoomImgUrl) {
        User chatRoomOwner = userUtils.findUserByUserId(userId);
        Space space = spaceUtils.findSpaceBySpaceId(spaceId);

        ChatRoom chatRoom = chatRoomModuleService.save(
                ChatRoom.of(space, createChatRoomRequest.getName(), chatRoomImgUrl));
        saveUserChatRooms(createChatRoomRequest, chatRoom, chatRoomOwner);

        return CreateChatRoomResponse.of(chatRoom.getId());
    }

    @Transactional
    public ReadChatRoomMemberResponse readChatRoomMembers(Long spaceId, Long chatRoomId) {
        List<UserInfoInSpace> userList = getUserInChatRoom(spaceId, chatRoomId);
        return ReadChatRoomMemberResponse.of(userList);
    }

    @Transactional
    public ChatSuccessResponse updateLastReadTime(Long userId, Long chatRoomId) {
        UserChatRoom targetChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoomId);

        targetChatRoom.setLastReadTime(LocalDateTime.now());
        userChatRoomModuleService.save(targetChatRoom);
        log.info("userId: " + userId + " socket disconnect 시 마지막으로 읽은 시간: " + targetChatRoom.getLastReadTime());

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse joinChatRoom(Long chatRoomId, JoinChatRoomRequest joinChatRoomRequest) {
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);
        List<Long> memberIdList = joinChatRoomRequest.getMemberList();

        handleJoinChatRoom(memberIdList, chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse modifyChatRoomName(Long chatRoomId, String name) {
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);

        chatRoomByChatRoomId.updateName(name);
        chatRoomModuleService.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse exitChatRoom(Long userId, Long chatRoomId) {
        UserChatRoom userChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoomId);

        userChatRoom.updateInactive();
        userChatRoomModuleService.save(userChatRoom);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);

        chatRoomByChatRoomId.updateInactive();
        chatRoomModuleService.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    private LastMessageInfoDto getLastMsgInfo(ChatRoom chatRoom) {
        ChatMessage lastMsg = chattingModuleService.findMostRecentMessage(chatRoom);
        LocalDateTime lastUpdateTime = lastMsg.getCreatedAt();
        HashMap<String, String> lastContent = lastMsg.getContent();

        log.info("마지막으로 업데이트된 시간: " + lastUpdateTime + " 마지막으로 읽은 내용 : " + lastContent);

        return LastMessageInfoDto.builder().lastUpdateTime(lastUpdateTime).lastContent(lastContent).build();
    }

    private int calculateUnreadMsgCount(Long userId, Long chatRoomId, LocalDateTime lastUpdateTime) {
        UserChatRoom userChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoomId);
        LocalDateTime lastReadTime = timeUtils.getEncodedTime(Objects.requireNonNull(userChatRoom.getLastReadTime()));
        log.info("마지막으로 읽은 시간: " + lastReadTime);

        int unreadMsgCount = chattingModuleService.countUnreadMessages(chatRoomId, lastReadTime, lastUpdateTime);
        log.info("안읽은 메시지 개수: " + unreadMsgCount);

        return unreadMsgCount;
    }

    private void saveUserChatRooms(CreateChatRoomRequest createChatRoomRequest, ChatRoom chatRoom, User chatRoomOwner) {
        userChatRoomModuleService.save(UserChatRoom.of(chatRoom, chatRoomOwner, LocalDateTime.now()));
        for (Long id : createChatRoomRequest.getMemberList()) {
            User chatRoomMember = userUtils.findUserByUserId(id);
            userChatRoomModuleService.save(UserChatRoom.of(chatRoom, chatRoomMember, LocalDateTime.now()));
        }
    }

    private List<UserInfoInSpace> getUserInChatRoom(Long spaceId, Long chatRoomId) {
        UserChatRooms userChatRooms = UserChatRooms.of(
                userChatRoomModuleService.findUserChatRooms(chatRoomId));

        return userChatRooms.getUsers().stream()
                .map(user -> {
                    UserSpace userSpace = userSpaceUtils.isUserInSpace(user.getUserId(), spaceId)
                            .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));

                    return new UserInfoInSpace(user.getUserId(), userSpace.getUserName(), userSpace.getUserProfileImg(),
                            userSpace.getUserSpaceAuth());
                })
                .toList();
    }

    private void handleJoinChatRoom(List<Long> memberIdList, ChatRoom chatRoom) {
        for (Long userId : Objects.requireNonNull(memberIdList)) {
            User userByUserId = userUtils.findUserByUserId(userId);

            if (isUserInChatRoom(userId, chatRoom.getId())) {
                if (userByUserId.getStatus().equals(BaseStatusType.INACTIVE)) {
                    updateAsActive(chatRoom, userId);
                    continue;
                }
                throw new CustomException(USER_IS_ALREADY_IN_CHATROOM);
            }

            userChatRoomModuleService.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));
        }
    }

    private boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        UserChatRooms userChatRooms = UserChatRooms.of(userChatRoomModuleService.findUserChatRooms(chatRoomId));
        return userChatRooms.isUserJoined(userId);
    }

    private void updateAsActive(ChatRoom chatRoom, Long userId) {
        UserChatRoom userChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoom.getId());
        userChatRoom.setUserRejoin();
        userChatRoomModuleService.save(userChatRoom);
    }
}
