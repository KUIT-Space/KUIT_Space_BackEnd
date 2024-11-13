package space.space_spring.domain.chat.chatroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.domain.chat.chatroom.model.response.ChatRoomResponse;
import space.space_spring.domain.chat.chatroom.model.response.ChatSuccessResponse;
import space.space_spring.domain.chat.chatroom.model.response.CreateChatRoomResponse;
import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomMemberResponse;
import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomResponse;
import space.space_spring.domain.chat.chatroom.repository.ChatRoomRepository;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.domain.chat.chatting.repository.ChattingRepository;
import space.space_spring.domain.chat.chatroom.repository.UserChatRoomRepository;
import space.space_spring.domain.chat.chatroom.model.dto.LastMessageInfoDto;
import space.space_spring.domain.chat.chatroom.model.request.JoinChatRoomRequest;
import space.space_spring.domain.chat.chatroom.model.request.CreateChatRoomRequest;
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
    private final SpaceUtils spaceUtils;
    private final TimeUtils timeUtils;
    private final UserSpaceDao userSpaceDao;
    private final ChattingRepository chattingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    @Transactional
    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 해당 user의 해당 space 내의 채팅방 리스트 return
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserAndSpace(userByUserId, spaceBySpaceId);

        return ReadChatRoomResponse.of(chatRoomList.stream()
                .map(cr -> {
                    // TODO 4: 각 채팅방의 마지막으로 업데이트된 메시지 정보 find
                    LastMessageInfoDto lastMsgInfo = getLastMsgInfo(cr);
                    LocalDateTime lastUpdateTime = lastMsgInfo.getLastUpdateTime();
                    HashMap<String, String> lastContent = lastMsgInfo.getLastContent();

                    // TODO 5: userChatRoom의 안읽은 메시지 개수 계산
                    int unreadMsgCount = calculateUnreadMsgCount(userByUserId, cr, lastUpdateTime);

                    return ChatRoomResponse.of(cr, lastContent, String.valueOf(lastUpdateTime), unreadMsgCount);
                })
                .filter(Objects::nonNull) // null 값을 제거
                .toList()
        );
    }

    @Transactional
    public CreateChatRoomResponse createChatRoom(Long userId, Long spaceId, CreateChatRoomRequest createChatRoomRequest, String chatRoomImgUrl) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: chatRoom 생성 및 저장
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(spaceBySpaceId, createChatRoomRequest.getName(), chatRoomImgUrl));

        // TODO 4: userChatRoom 매핑 정보 저장
        userChatRoomRepository.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));
        for (Long id : createChatRoomRequest.getMemberList()) {
            User user = userUtils.findUserByUserId(id);
            userChatRoomRepository.save(UserChatRoom.of(chatRoom, user, LocalDateTime.now()));
        }

        // TODO 5: chatroom id 반환
        return CreateChatRoomResponse.of(chatRoom.getId());
    }

    @Transactional
    public ReadChatRoomMemberResponse readChatRoomMembers(Long spaceId, Long chatRoomId) {

        // TODO 1: spaceId에 해당하는 space find
        Space spaceById = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 3: 해당 userChatRoom의 user들 find
        List<UserInfoInSpace> userList = getUserInChatRoom(spaceById, chatRoomByChatRoomId);

        return ReadChatRoomMemberResponse.of(userList);
    }

    @Transactional
    public ChatSuccessResponse updateLastReadTime(Long userId, Long chatRoomId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 3: 해당 user와 chatRoom에 대한 userChatRoom find
        UserChatRoom targetChatRoom = userChatRoomRepository.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, BaseStatusType.ACTIVE);

        // TODO 4: userChatRoom의 마지막으로 읽은 시간 저장
        targetChatRoom.setLastReadTime(LocalDateTime.now());
        userChatRoomRepository.save(targetChatRoom);
        log.info("userId: " + userId + " socket disconnect 시 마지막으로 읽은 시간: " + targetChatRoom.getLastReadTime());

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse joinChatRoom(Long chatRoomId, JoinChatRoomRequest joinChatRoomRequest) {
        List<Long> memberIdList = joinChatRoomRequest.getMemberList();

        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 2: 유저들의 채팅방 초대 이력에 따른 재초대
        handleJoinChatRoom(memberIdList, chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse modifyChatRoomName(Long chatRoomId, String name) {
        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 2: 채팅방 이름 변경
        chatRoomByChatRoomId.updateName(name);
        chatRoomRepository.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse exitChatRoom(Long userId, Long chatRoomId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 3: 해당 user와 chatRoom에 대한 userChatRoom find
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, BaseStatusType.ACTIVE);

        // TODO 4: 해당 userChatRoom inactive로 변경
        userChatRoom.updateInactive();
        userChatRoomRepository.save(userChatRoom);

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse deleteChatRoom(Long chatRoomId) {
        // TODO 1: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = Optional.ofNullable(
                        chatRoomRepository.findByIdAndStatus(chatRoomId, BaseStatusType.ACTIVE))
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_EXIST));

        // TODO 2: 해당 chatRoom inactive로 변경
        chatRoomByChatRoomId.updateInactive();
        chatRoomRepository.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    private boolean isUserInChatRoom(User userByUserId, ChatRoom chatRoomByChatRoomId) {
        List<UserChatRoom> chatRoomList = userChatRoomRepository.findByChatRoomAndStatus(chatRoomByChatRoomId, BaseStatusType.ACTIVE);
        return chatRoomList.stream().anyMatch(userChatRoom -> userChatRoom.getUser().equals(userByUserId));
    }

    private LastMessageInfoDto getLastMsgInfo(ChatRoom chatRoom) {
        ChatMessage lastMsg = chattingRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
        LocalDateTime lastUpdateTime = lastMsg != null ? lastMsg.getCreatedAt() :timeUtils.getEncodedTime(chatRoom.getCreatedAt());
        HashMap<String, String> lastContent = lastMsg != null ? lastMsg.getContent() : new HashMap<>(Map.of("text", "메시지를 전송해보세요"));

        log.info("마지막으로 업데이트된 시간: " + lastUpdateTime + " 마지막으로 읽은 내용 : " + lastContent);

        return LastMessageInfoDto.builder().lastUpdateTime(lastUpdateTime).lastContent(lastContent).build();
    }

    private int calculateUnreadMsgCount(User userByUserId, ChatRoom chatRoom, LocalDateTime lastUpdateTime) {
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoomAndStatus(userByUserId, chatRoom, BaseStatusType.ACTIVE);
        LocalDateTime lastReadTime = timeUtils.getEncodedTime(userChatRoom.getLastReadTime()); // LocalDateTime으로 변환
        log.info("마지막으로 읽은 시간: " + lastReadTime);

        int unreadMsgCount = chattingRepository.countByChatRoomIdAndCreatedAtBetween(
                chatRoom.getId(),
                lastReadTime,
                lastUpdateTime
        );
        log.info("안읽은 메시지 개수: " + unreadMsgCount);

        return unreadMsgCount;
    }

    private List<UserInfoInSpace> getUserInChatRoom(Space space, ChatRoom chatRoom) {
        List<UserInfoInSpace> userList = new ArrayList<>();
        List<UserChatRoom> userChatRoomList = userChatRoomRepository.findByChatRoomAndStatus(chatRoom, BaseStatusType.ACTIVE);

        userChatRoomList.forEach(userChatRoom -> {
            User user = userChatRoom.getUser();

            // TODO 1: 스페이스 프로필 이미지 get 위해 userSpace find
            UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(user, space)
                    .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));

            // TODO 2: user 정보 추가
            userList.add(new UserInfoInSpace(user.getUserId(), userSpace.getUserName(), userSpace.getUserProfileImg(), userSpace.getUserSpaceAuth()));
        });

        return userList;
    }

    private void handleJoinChatRoom(List<Long> memberIdList, ChatRoom chatRoomByChatRoomId) {
        for (Long memberId : Objects.requireNonNull(memberIdList)) {
            User userByUserId = userUtils.findUserByUserId(memberId);

            // TODO 1: 유저가 채팅방에 초대된 적이 있는 경우 다시 초대
            if (isUserInChatRoom(userByUserId, chatRoomByChatRoomId)) {
                // TODO 2: 유저가 채팅방에 초대되었다가 나간 경우 다시 초대
                if (userByUserId.getStatus().equals(BaseStatusType.INACTIVE)) {
                    UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, BaseStatusType.ACTIVE);
                    userChatRoom.setUserRejoin();
                    userChatRoomRepository.save(userChatRoom);
                    continue;
                }
                // TODO 3: 유저가 채팅방에 초대되어서 참가하고 있는 경우 예외 발생
                throw new CustomException(USER_IS_ALREADY_IN_CHAT_ROOM);
            }

            // TODO 4: 채팅방에 초대된 적이 없는 유저에 대한 userChatRoom 테이블 생성
            userChatRoomRepository.save(UserChatRoom.of(chatRoomByChatRoomId, userByUserId, LocalDateTime.now()));
        }
    }
}
