package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.ChattingDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.dto.chat.request.JoinChatRoomRequest;
import space.space_spring.dto.chat.response.*;
import space.space_spring.dto.chat.request.CreateChatRoomRequest;
import space.space_spring.dto.userSpace.UserInfoInSpace;
import space.space_spring.entity.*;
import space.space_spring.entity.document.ChatMessage;

import space.space_spring.exception.CustomException;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final UserSpaceDao userSpaceDao;
    private final ChattingDao chattingDao;
    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;

    @Transactional
    public ReadChatRoomResponse readChatRooms(Long userId, Long spaceId) {
        // TODO 1: userId에 해당하는 user find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2: spaceId에 해당하는 space find
        Space spaceBySpaceId = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 3: 해당 user의 해당 space 내의 active 채팅방 리스트 return
        List<ChatRoom> chatRoomList = chatRoomDao.findByUserAndSpace(userByUserId, spaceBySpaceId);

        return ReadChatRoomResponse.of(chatRoomList.stream()
                .map(cr -> {
                    // TODO 4: userChatRoom active인지 확인
                    UserChatRoom userChatRoom = userChatRoomDao.findByUserAndChatRoomAndStatus(userByUserId, cr, "ACTIVE");
                    if (!userChatRoom.getStatus().equals("ACTIVE")) {
                        return null;
                    }

                    // TODO 5: 각 채팅방의 마지막으로 업데이트된 메시지 정보 find
                    ChatMessage lastMsg = chattingDao.findTopByChatRoomIdOrderByCreatedAtDesc(cr.getId());

                    LocalDateTime lastUpdateTime = cr.getEncodedTime();
                    HashMap<String, String> lastContent = new HashMap<>();
                    lastContent.put("text", "메시지를 전송해보세요");

                    if (lastMsg != null) {
                        lastUpdateTime = lastMsg.getCreatedAt();
                        lastContent = lastMsg.getContent();
                        log.info("마지막으로 업데이트된 시간: " + lastUpdateTime + " 마지막으로 읽은 내용 : " + lastContent);
                    }

                    // TODO 6: 각 채팅방의 안읽은 메시지 개수 계산
                    LocalDateTime lastReadTime = userChatRoom.getEncodedTime(); // LocalDateTime으로 변환

                    log.info("마지막으로 읽은 시간: " + lastReadTime);
                    int unreadMsgCount = chattingDao.countByChatRoomIdAndCreatedAtBetween(
                            cr.getId(),
                            lastReadTime,
                            lastUpdateTime
                    );
                    log.info("안읽은 메시지 개수: " + unreadMsgCount);

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
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.of(spaceBySpaceId, createChatRoomRequest.getName(), chatRoomImgUrl));

        // TODO 4: user_chatRoom 매핑 정보 저장
        userChatRoomDao.save(UserChatRoom.of(chatRoom, userByUserId, LocalDateTime.now()));
        for (Long id : createChatRoomRequest.getMemberList()) {
            User user = userUtils.findUserByUserId(id);
            userChatRoomDao.save(UserChatRoom.of(chatRoom, user, LocalDateTime.now()));
        }

        // TODO 5: chatroom id 반환
        return CreateChatRoomResponse.of(chatRoom.getId());
    }

    @Transactional
    public ReadChatRoomMemberResponse readChatRoomMembers(Long spaceId, Long chatRoomId) {
        List<UserInfoInSpace> userList = new ArrayList<>();

        // TODO 1: spaceId에 해당하는 space find
        Space spaceById = spaceUtils.findSpaceBySpaceId(spaceId);

        // TODO 2: chatRoomId에 해당하는 chatRoom find
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");
        // TODO: null일 때 CHATROOM_NOT_EXIST exception 처리

        // TODO 3: 해당 chatRoom의 userChatRoom 리스트 find
        List<UserChatRoom> userChatRoomList = userChatRoomDao.findByChatRoomAndStatus(chatRoomByChatRoomId, "ACTIVE");

        userChatRoomList.forEach(userChatRoom -> {
            User user = userChatRoom.getUser();

            // TODO 4: 스페이스 프로필 이미지 get 위해 userSpace find
            UserSpace userSpace = userSpaceDao.findUserSpaceByUserAndSpace(user, spaceById)
                    .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));

            userList.add(new UserInfoInSpace(user.getUserId(), userSpace.getUserName(), userSpace.getUserProfileImg(), userSpace.getUserSpaceAuth()));
        });

        return ReadChatRoomMemberResponse.of(userList);
    }

    @Transactional
    public ChatSuccessResponse updateLastReadTime(Long userId, Long chatRoomId) {
        User userByUserId = userUtils.findUserByUserId(userId);
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");

        UserChatRoom targetChatRoom = userChatRoomDao.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, "ACTIVE");
        targetChatRoom.setLastReadTime(LocalDateTime.now());
        userChatRoomDao.save(targetChatRoom);
        log.info("userId: " + userId + " socket disconnect 시 마지막으로 읽은 시간: " + targetChatRoom.getLastReadTime());

        return ChatSuccessResponse.of(true);
    }

    @Transactional
    public ChatSuccessResponse joinChatRoom(Long chatRoomId, JoinChatRoomRequest joinChatRoomRequest) {
        List<Long> memberIdList = joinChatRoomRequest.getMemberList();
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");

        for (Long memberId : Objects.requireNonNull(memberIdList)) {
            // TODO 1: 초대한 유저 조회
            User userByUserId = userUtils.findUserByUserId(memberId);

            // TODO 2: 유저가 이미 채팅방에 초대되어있는지 검증
            if (isUserInChatRoom(userByUserId, chatRoomByChatRoomId)) {
                // TODO 3: 유저가 채팅방에 초대된 이력이 있다면 userChatRoom의 status 변경
                if (userByUserId.getStatus().equals("INACTIVE")) {
                    UserChatRoom userChatRoom = userChatRoomDao.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, "ACTIVE");
                    userChatRoom.setUserRejoin();
                    userChatRoomDao.save(userChatRoom);
                    return ChatSuccessResponse.of(true);
                } else {
                    // TODO 4: 유저가 채팅방에 초대되어있고 ACTIVE이므로 예외 발생
                    throw new CustomException(USER_IS_ALREADY_IN_CHAT_ROOM);
                }
            }

            // TODO 5: 초대한 유저에 대한 userChatRoom 테이블 생성
            userChatRoomDao.save(UserChatRoom.of(chatRoomByChatRoomId, userByUserId, LocalDateTime.now()));
        }

        return ChatSuccessResponse.of(true);
    }

    public ChatSuccessResponse modifyChatRoomName(Long chatRoomId, String name) {
        // TODO 1: 해당 채팅방 find
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");

        // TODO 2: 채팅방 이름 변경
        chatRoomByChatRoomId.updateName(name);
        chatRoomDao.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    public ChatSuccessResponse exitChatRoom(Long userId, Long chatRoomId) {
        User userByUserId = userUtils.findUserByUserId(userId);
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");

        // TODO 1: 해당 userChatRoom find
        UserChatRoom userChatRoom = userChatRoomDao.findByUserAndChatRoomAndStatus(userByUserId, chatRoomByChatRoomId, "ACTIVE");

        // TODO 2: 해당 userChatRoom inactive로 변경
        userChatRoom.updateInactive();

        // TODO 3: DB에 변경 사항 저장
        userChatRoomDao.save(userChatRoom);

        return ChatSuccessResponse.of(true);
    }

    public ChatSuccessResponse deleteChatRoom(Long chatRoomId) {
        // TODO 1: 해당 chatRoom find
        ChatRoom chatRoomByChatRoomId = chatRoomDao.findByIdAndStatus(chatRoomId, "ACTIVE");

        // TODO 2: 해당 chatRoom inactive로 변경
        chatRoomByChatRoomId.updateInactive();

        // TODO 3: DB에 변경 사항 저장
        chatRoomDao.save(chatRoomByChatRoomId);

        return ChatSuccessResponse.of(true);
    }

    private boolean isUserInChatRoom(User userByUserId, ChatRoom chatRoomByChatRoomId) {
        List<UserChatRoom> chatRoomList = userChatRoomDao.findByChatRoomAndStatus(chatRoomByChatRoomId, "ACTIVE");
        return chatRoomList.stream().anyMatch(userChatRoom -> userChatRoom.getUser().equals(userByUserId));
    }
}
