package space.space_spring.domain.post.adapter.in.web.createPost;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestOfCreatePost extends SelfValidating<RequestOfCreatePost> {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
    private String content;

    @Valid
    @Nullable
    private List<AttachmentOfCreate> attachments;

    @NotBlank(message = "게시글의 익명/비익명 여부는 공백일 수 없습니다.")
    private Boolean isAnonymous; // 질문일 경우만 사용

    public RequestOfCreatePost(String title, String content, List<AttachmentOfCreate> attachments, Boolean isAnonymous) {
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.isAnonymous = isAnonymous;
        this.validateSelf();
    }

}
