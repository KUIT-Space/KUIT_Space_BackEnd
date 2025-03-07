package space.space_spring.domain.post.adapter.out.persistence.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.application.port.out.LoadAttachmentPort;
import space.space_spring.domain.post.application.port.out.UploadAttachmentPort;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.S3Uploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements LoadAttachmentPort, UploadAttachmentPort {

    private final SpringDataAttachmentRepository attachmentRepository;
    private final S3Uploader s3Uploader;

    @Override
    public Map<Long, String> findFirstImageByPostIds(List<Long> postIds) {
        List<AttachmentSummary> images = attachmentRepository.findImagesByPostIds(postIds, AttachmentType.IMAGE);
        return images.stream()
                .collect(Collectors.toMap(
                        AttachmentSummary::getPostId,
                        AttachmentSummary::getAttachmentUrl,
                        (existing, replacement) -> existing     // 중복된 postId 중 첫 번째만 유지
                ));
    }

    @Override
    public List<String> uploadAllAttachments(Map<AttachmentType, List<MultipartFile>> map, String dirName) {
        List<String> savedAttachmentUrls = new ArrayList<>();

        for (Map.Entry<AttachmentType, List<MultipartFile>> entry : map.entrySet()) {
            AttachmentType key = entry.getKey();

            if (key == AttachmentType.IMAGE) {
                for (MultipartFile multipartFile : entry.getValue()) {
                    if (!s3Uploader.isImageFile(multipartFile)) {
                        throw new CustomException(IS_NOT_IMAGE_FILE);
                    }

                    try {
                        savedAttachmentUrls.add(s3Uploader.upload(multipartFile, dirName + "Img"));
                    } catch (IOException e) {
                        throw new CustomException(MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY);
                    }
                }
            } else {
                for (MultipartFile multipartFile : entry.getValue()) {
                    if (!s3Uploader.isDocumentFile(multipartFile)) {
                        throw new CustomException(IS_NOT_DOCUMENT_FILE);
                    }

                    try {
                        savedAttachmentUrls.add(s3Uploader.upload(multipartFile, dirName + "Document"));
                    } catch (IOException e) {
                        throw new CustomException(MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY);
                    }
                }
            }
        }

        return savedAttachmentUrls;
    }
}
