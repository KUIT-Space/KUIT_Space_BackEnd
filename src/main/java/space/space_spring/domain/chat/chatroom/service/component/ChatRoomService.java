package space.space_spring.domain.chat.chatroom.service.component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
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

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserUtils userUtils;
    // findUserByUserId -> common module service
    private final SpaceUtils spaceUtils;
    // findSpaceBySpaceId -> common module service
    private final UserSpaceDao userSpaceDao;
    // findUserSpaceByUserAndSpace -> common module service

    private final ChatRoomModuleService chatRoomModuleService;
    private final ChattingModuleService chattingModuleService;
    private final UserChatRoomModuleService userChatRoomModuleService;
    private final TimeUtils timeUtils;

    @Transactional
    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        // TODO 3: 해당 user의 해당 space 내의 채팅방 리스트 return
        ChatRooms chatRooms = ChatRooms.of(chatRoomModuleService.findChatRooms(userId, spaceId));

        List<ChatRoomResponse> chatRoomResponses = chatRooms.toChatRoomResponses(
                userId,
                this::getLastMsgInfo,
                (user, chatRoom) -> calculateUnreadMsgCount(user, chatRoom.getId(),
                        getLastMsgInfo(chatRoom).getLastUpdateTime())
        );

        return ReadChatRoomResponse.of(chatRoomResponses);
    }


    @Transactional
    public CreateChatRoomResponse createChatRoom(Long userId, Long spaceId, CreateChatRoomRequest createChatRoomRequest,
                                                 String chatRoomImgUrl) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: chatRoom 생성 및 저장
        ChatRoom chatRoom = chatRoomModuleService.save(
                ChatRoom.of(spaceBySpaceId, createChatRoomRequest.getName(), chatRoomImgUrl));

        // TODO 4: userChatRoom 매핑 정보 저장
        userChatRoomModuleService.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));
        for (Long id : createChatRoomRequest.getMemberList()) {
            User user = userUtils.findUserByUserId(id);
            userChatRoomModuleService.save(UserChatRoom.of(chatRoom, user, LocalDateTime.now()));
        }

        // TODO 5: chatroom id 반환
        return CreateChatRoomResponse.of(chatRoom.getId());
    }

    @Transactional
    public ReadChatRoomMemberResponse readChatRoomMembers(Long spaceId, Long chatRoomId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceById = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 해당 userChatRoom의 user들 find
        List<UserInfoInSpace> userList = getUserInChatRoom(spaceById, chatRoomId);

        return ReadChatRoomMemberResponse.of(userList);
    }

    @Transactional
    public ChatSuccessResponse updateLastReadTime(Long userId, Long chatRoomId) {
        // TODO 3: 해당 user와 chatRoom에 대한 userChatRoom find
        UserChatRoom targetChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoomId);

        // TODO 4: userChatRoom의 마지막으로 읽은 시간 저장
        targetChatRoom.setLastReadTime(LocalDateTime.now());
        userChatRoomModuleService.save(targetChatRoom);
        log.info("userId: " + userId + " socket disconnect 시 마지막으로 읽은 시간: " + targetChatRoom.getLastReadTime());

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse joinChatRoom(Long chatRoomId, JoinChatRoomRequest joinChatRoomRequest) {
        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);

        // TODO 2: 유저들의 채팅방 초대 이력에 따른 재초대
        List<Long> memberIdList = joinChatRoomRequest.getMemberList();
        handleJoinChatRoom(memberIdList, chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse modifyChatRoomName(Long chatRoomId, String name) {
        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);

        // TODO 2: 채팅방 이름 변경
        chatRoomByChatRoomId.updateName(name);
        chatRoomModuleService.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse exitChatRoom(Long userId, Long chatRoomId) {
        // TODO 3: 해당 user와 chatRoom에 대한 userChatRoom find
        UserChatRoom userChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoomId);

        // TODO 4: 해당 userChatRoom inactive로 변경
        userChatRoom.updateInactive();
        userChatRoomModuleService.save(userChatRoom);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse deleteChatRoom(Long chatRoomId) {
        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = chatRoomModuleService.findChatRoom(chatRoomId);

        // TODO 2: 해당 chatRoom inactive로 변경
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
        LocalDateTime lastReadTime = timeUtils.getEncodedTime(userChatRoom.getLastReadTime()); // LocalDateTime으로 변환
        log.info("마지막으로 읽은 시간: " + lastReadTime);

        int unreadMsgCount = chattingModuleService.countUnreadMessages(
                chatRoomId,
                lastReadTime,
                lastUpdateTime
        );
        log.info("안읽은 메시지 개수: " + unreadMsgCount);

        return unreadMsgCount;
    }

    private List<UserInfoInSpace> getUserInChatRoom(Space space, Long chatRoomId) {
        UserChatRooms userChatRooms = UserChatRooms.of(
                userChatRoomModuleService.findUserChatRooms(chatRoomId));

        return userChatRooms.getUsers().stream()
                .map(user -> {
                    UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(user, space)
                            .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));

                    return new UserInfoInSpace(
                            user.getUserId(),
                            userSpace.getUserName(),
                            userSpace.getUserProfileImg(),
                            userSpace.getUserSpaceAuth());
                })
                .toList();
    }

    private void handleJoinChatRoom(List<Long> memberIdList, ChatRoom chatRoom) {
        for (Long userId : Objects.requireNonNull(memberIdList)) {
            User userByUserId = userUtils.findUserByUserId(userId);

            // TODO 1: 유저가 채팅방에 초대된 적이 있는 경우 다시 초대
            if (isUserInChatRoom(userId, chatRoom.getId())) {
                // TODO 2: 유저가 채팅방에 초대되었다가 나간 경우 다시 초대
                if (userByUserId.getStatus().equals(BaseStatusType.INACTIVE)) {
                    UserChatRoom userChatRoom = userChatRoomModuleService.findUserChatRoom(userId, chatRoom.getId());
                    userChatRoom.setUserRejoin();
                    userChatRoomModuleService.save(userChatRoom);
                    continue;
                }
                // TODO 3: 유저가 채팅방에 초대되어서 참가하고 있는 경우 예외 발생
                throw new CustomException(USER_IS_ALREADY_IN_CHATROOM);
            }

            // TODO 4: 채팅방에 초대된 적이 없는 유저에 대한 userChatRoom 테이블 생성
            userChatRoomModuleService.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));
        }
    }

    private boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        UserChatRooms userChatRooms = UserChatRooms.of(userChatRoomModuleService.findUserChatRooms(chatRoomId));
        return userChatRooms.isUserJoined(userId);
    }
}
