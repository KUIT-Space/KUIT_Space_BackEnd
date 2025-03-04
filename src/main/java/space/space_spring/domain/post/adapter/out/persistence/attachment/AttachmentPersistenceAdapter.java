package space.space_spring.domain.post.adapter.out.persistence.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.LoadAttachmentPort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements LoadAttachmentPort {

    private final SpringDataAttachmentRepository attachmentRepository;

    @Override
    public Map<Long, String> findFirstImageByPostIds(List<Long> postIds) {
        List<AttachmentSummary> images = attachmentRepository.findImagesByPostIds(postIds);
        return images.stream()
                .collect(Collectors.toMap(
                        AttachmentSummary::getPostId,
                        AttachmentSummary::getAttachmentUrl,
                        (existing, replacement) -> existing     // 중복된 postId 중 첫 번째만 유지
                ));
    }
}
