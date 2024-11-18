package space.space_spring.domain.chat.chatting.service;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import space.space_spring.domain.chat.chatting.repository.ChattingRepository;
import space.space_spring.domain.chat.chatting.model.request.ChatMessageRequest;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageLogResponse;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.entity.enumStatus.BaseStatusType;
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

    private final static String TYPE_IMAGE = "image";
    private final static String TYPE_FILE = "file";
    private final static String IMAGE_DIR_NAME = "chattingImg";
    private final static String FILE_DIR_NAME = "chattingFile";

    @Transactional
    public ChatMessageResponse sendChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId)
            throws IOException {
        UserSpace senderInSpace = userSpaceUtils.isUserInSpace(senderId, chatMessageRequest.getSpaceId())
                .orElseThrow(() -> new CustomException(USER_IS_NOT_IN_SPACE));
        String senderName = senderInSpace.getUserName();
        String senderProfileImg = senderInSpace.getUserProfileImg();

        saveFileUrl(chatMessageRequest);

        ChatMessage message = saveChatMessage(senderId, chatMessageRequest, chatRoomId, senderName, senderProfileImg);

        return ChatMessageResponse.create(message);
    }

    public ChatMessageLogResponse readChatMessageLog(Long chatRoomId) {
        List<ChatMessage> chatMessageList = chattingRepository.findByChatRoomIdAndStatus(chatRoomId,
                BaseStatusType.ACTIVE);
        return ChatMessageLogResponse.of(chatMessageList.stream()
                .map(ChatMessageResponse::create)
                .collect(Collectors.toList()));
    }

    private void saveFileUrl(ChatMessageRequest chatMessageRequest) throws IOException {
        String fileUrl = createFileUrl(chatMessageRequest);
        setFileUrl(chatMessageRequest, fileUrl);
    }

    private String createFileUrl(ChatMessageRequest chatMessageRequest) throws IOException {
        HashMap<String, String> chatMessageContent = chatMessageRequest.getContent();

        String base64File = "", dirName = "", fileName = "";
        switch (chatMessageRequest.getMessageType()) {
            case IMG -> {
                base64File = chatMessageContent.get(TYPE_IMAGE);
                dirName = IMAGE_DIR_NAME;
                fileName = "img";
            }
            case FILE -> {
                base64File = chatMessageContent.get(TYPE_FILE);
                dirName = FILE_DIR_NAME;
                fileName = chatMessageContent.get("fileName");
            }
            default -> {
            }
        }

        return s3Uploader.uploadBase64File(base64File, dirName, fileName);
    }

    private static void setFileUrl(ChatMessageRequest chatMessageRequest, String s3Url) {
        if (!s3Url.isEmpty()) {
            if (chatMessageRequest.getMessageType().equals(ChatMessageType.IMG)) {
                chatMessageRequest.getContent().put(TYPE_IMAGE, s3Url);
                return;
            }
            chatMessageRequest.getContent().put(TYPE_FILE, s3Url);
        }
    }

    @NotNull
    private ChatMessage saveChatMessage(Long senderId, ChatMessageRequest chatMessageRequest, Long chatRoomId,
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
