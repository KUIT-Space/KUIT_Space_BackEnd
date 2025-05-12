package space.space_spring.domain.post.adapter.in.web.updatePost;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.global.common.validation.SelfValidating;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequestOfUpdatePost extends SelfValidating<RequestOfUpdatePost> {

    @NotBlank(message = "게시글 제목은 공백일 수 없습니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 공백일 수 없습니다.")
    private String content;

    @Nullable
    private List<MultipartFile> newAttachments = new ArrayList<>();

    @Nullable
    private List<String> removeAttachmentUrls = new ArrayList<>();

    @Nullable
    private List<Long> tagIds = new ArrayList<>();
}
