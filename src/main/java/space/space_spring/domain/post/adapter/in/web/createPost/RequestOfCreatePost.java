package space.space_spring.domain.post.adapter.in.web.createPost;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.global.common.validation.SelfValidating;

import java.util.List;

@Getter
@Setter     // modelAttribute 의 구성을 위해
@NoArgsConstructor
public class RequestOfCreatePost extends SelfValidating<RequestOfCreatePost> {

    @Nullable
    private List<Long> tagIds;

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
    private String content;

    @Valid
    @Nullable
    private List<MultipartFile> attachments;

    @NotNull(message = "게시글의 익명/비익명 여부는 공백일 수 없습니다.")
    private Boolean isAnonymous; // 질문일 경우만 사용


    public RequestOfCreatePost(List<Long> tagIds, String title, String content, List<MultipartFile> attachments, Boolean isAnonymous) {
        this.tagIds = tagIds;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.isAnonymous = isAnonymous;
        this.validateSelf();
    }

}
