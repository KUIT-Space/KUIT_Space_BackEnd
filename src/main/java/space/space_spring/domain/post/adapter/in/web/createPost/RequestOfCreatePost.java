package space.space_spring.domain.post.adapter.in.web.createPost;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestOfCreatePost extends SelfValidating<RequestOfCreatePost> {

    @NotBlank(message = "title은 공백일 수 없습니다.")
    private String title;

    @NotBlank(message = "content는 공백일 수 없습니다.")
    private String content;

    @Valid
    private List<AttachmentOfCreate> attachments;

    public RequestOfCreatePost(String title, String content, List<AttachmentOfCreate> attachments) {
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.validateSelf();
    }

}
