package space.space_spring.domain.chat.chatting.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
    public ChatMessageResponse sendChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId)
            throws IOException {
        UserSpace senderInSpace = userSpaceUtils.isUserInSpace(senderId, chatMessageRequest.getSpaceId())
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));
        String senderName = senderInSpace.getUserName();
        String senderProfileImg = senderInSpace.getUserProfileImg();

        setFileUrl(chatMessageRequest);

        ChatMessage message = createChatMessage(senderId, chatMessageRequest, chatRoomId, senderName, senderProfileImg);

        return ChatMessageResponse.create(message);
    }

    public ChatMessageLogResponse readChatMessageLog(Long chatRoomId) {
        List<ChatMessage> chatMessageList = chattingRepository.findByChatRoomId(chatRoomId);
        return ChatMessageLogResponse.of(chatMessageList.stream()
                .map(ChatMessageResponse::create)
                .collect(Collectors.toList()));
    }

    private void setFileUrl(ChatMessageRequest chatMessageRequest) throws IOException {
        String fileUrl = createFileUrl(chatMessageRequest);
        saveFileUrl(chatMessageRequest, fileUrl);
    }

    private String createFileUrl(ChatMessageRequest chatMessageRequest) throws IOException {
        String fileUrl = switch (chatMessageRequest.getMessageType()) {
            case IMG -> s3Uploader.uploadBase64File(chatMessageRequest.getContent().get("image"), "chattingImg", "img");
            case FILE -> s3Uploader.uploadBase64File(chatMessageRequest.getContent().get("file"), "chattingFile",
                    chatMessageRequest.getContent().get("fileName"));
            default -> "";
        };
        return fileUrl;
    }

    private static void saveFileUrl(ChatMessageRequest chatMessageRequest, String s3Url) {
        if (!Objects.equals(s3Url, "")) {
            if (chatMessageRequest.getMessageType().equals(ChatMessageType.IMG)) {
                chatMessageRequest.getContent().put("image", s3Url);
                return;
            }
            chatMessageRequest.getContent().put("file", s3Url);
        }
    }

    @NotNull
    private ChatMessage createChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId,
                                          String senderName, String senderProfileImg) {
        return chattingRepository.insert(ChatMessage.create(
                chatMessageRequest.getContent(),
                chatRoomId,
                chatMessageRequest.getSpaceId(),
                senderId,
                senderName,
                senderProfileImg,
                chatMessageRequest.getMessageType()
        ));
    }
}
