package space.space_spring.domain.chat.chatting.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.chat.chatting.repository.ChattingRepository;
import space.space_spring.domain.chat.chatting.model.request.ChatMessageRequest;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageLogResponse;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.entity.enumStatus.ChatMessageType;
import space.space_spring.exception.CustomException;
import space.space_spring.service.S3Uploader;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;


@Service
@RequiredArgsConstructor
public class ChattingService {

    private final S3Uploader s3Uploader;
    private final UserSpaceUtils userSpaceUtils;
    private final ChattingRepository chattingRepository;

    @Transactional
    public ChatMessageResponse sendChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId) throws IOException {

        // TODO 1: 메시지 메타데이터 저장 위해 전송자 찾기
        UserSpace senderInSpace = userSpaceUtils.isUserInSpace(senderId, chatMessageRequest.getSpaceId())
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));

        // TODO 2: validation 후 전송자 이름 및 프로필 사진 get
        String senderName = senderInSpace.getUserName();
        String senderProfileImg = senderInSpace.getUserProfileImg();

        // TODO 3: 이미지 및 파일 포함하는 경우 S3 업로드
        String s3Url = switch (chatMessageRequest.getMessageType()) {
            case IMG -> s3Uploader.uploadBase64File(chatMessageRequest.getContent().get("image") , "chattingImg", "img");
            case FILE -> s3Uploader.uploadBase64File(chatMessageRequest.getContent().get("file") , "chattingFile", chatMessageRequest.getContent().get("fileName"));
            default -> "";
        };

        // TODO 4: DB에 S3 url 저장 위한 content 처리
        if (!s3Url.isEmpty()) {
            if (chatMessageRequest.getMessageType().equals(ChatMessageType.IMG)) {
                chatMessageRequest.getContent().put("image", s3Url);
            } else {
                chatMessageRequest.getContent().put("file", s3Url);
            }
        }

        // TODO 4: DB에 메시지 저장
        ChatMessage message = chattingRepository.insert(ChatMessage.create(
                chatMessageRequest.getContent(),
                chatRoomId,
                chatMessageRequest.getSpaceId(),
                senderId,
                senderName,
                senderProfileImg,
                chatMessageRequest.getMessageType()
        ));

        return ChatMessageResponse.create(message);
    }

    public ChatMessageLogResponse readChatMessageLog(Long chatRoomId) {
        List<ChatMessage> chatMessageList = chattingRepository.findByChatRoomId(chatRoomId);
        return ChatMessageLogResponse.of(chatMessageList.stream()
                .map(ChatMessageResponse::create)
                .collect(Collectors.toList()));
    }
}
