package space.space_spring.global.common.enumStatus;

import lombok.Getter;

@Getter
public enum ChatMessageType {
    TEXT("text"),
    IMG("img"),
    FILE("file"),
    POST("post"),
    PAY("pay");

    private String chatMessageType;

    ChatMessageType(String chatMessageType) {
        this.chatMessageType = chatMessageType;
    }
}
