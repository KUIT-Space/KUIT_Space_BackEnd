package space.space_spring.domain.post.adapter.out.persistence.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.Attachment;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.S3Uploader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class AttachmentPersistenceAdapter implements LoadAttachmentPort, UploadAttachmentPort, CreateAttachmentPort, DeleteAttachmentPort {

    private final SpringDataAttachmentRepository attachmentRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final S3Uploader s3Uploader;
    private final AttachmentMapper attachmentMapper;

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
    public List<String> loadAttachmentUrlByTargetId(Long targetId) {
        Optional<List<AttachmentJpaEntity>> allByPostBaseIdAndStatus = attachmentRepository.findAllByPostBaseIdAndStatus(targetId, BaseStatusType.ACTIVE);

        if (allByPostBaseIdAndStatus.isPresent()) {
            return new ArrayList<>();
        }

        List<String> attachmentUrls = new ArrayList<>();
        for (AttachmentJpaEntity attachment : allByPostBaseIdAndStatus.get()) {
            attachmentUrls.add(attachment.getAttachmentUrl());
        }

        return attachmentUrls;
    }

    @Override
    public List<Attachment> loadByPostId(Long postId) {
        // 1. DB에서 ACTIVE 상태의 첨부파일 조회
        List<AttachmentJpaEntity> attachmentJpaEntities = attachmentRepository.findByPostBaseIdAndStatus(postId, BaseStatusType.ACTIVE);

        // 2. JPA 엔티티 → 도메인 엔티티로 변환하여 반환
        return attachmentJpaEntities.stream()
                .map(attachmentMapper::toDomainEntity)
                .toList();
    }

    @Override
    public Map<AttachmentType, List<String>> uploadAllAttachments(Map<AttachmentType, List<MultipartFile>> map, String dirName) {
        Map<AttachmentType, List<String>> savedAttachmentUrls = new HashMap<>();

        for (Map.Entry<AttachmentType, List<MultipartFile>> entry : map.entrySet()) {
            AttachmentType key = entry.getKey();

            for (MultipartFile multipartFile : entry.getValue()) {
                try {
                    String uploadedUrl;
                    if (key == AttachmentType.IMAGE) {          // 이미지 파일
                        if (!s3Uploader.isImageFile(multipartFile)) {
                            throw new CustomException(IS_NOT_IMAGE_FILE);
                        }
                        uploadedUrl = s3Uploader.upload(multipartFile, dirName + "Img");
                    } else {            // 문서 파일
                        if (!s3Uploader.isDocumentFile(multipartFile)) {
                            throw new CustomException(IS_NOT_DOCUMENT_FILE);
                        }
                        uploadedUrl = s3Uploader.upload(multipartFile, dirName + "Document");
                    }

                    savedAttachmentUrls.computeIfAbsent(key, k -> new ArrayList<>()).add(uploadedUrl);                } catch (IOException e) {
                    throw new CustomException(MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY);
                }
            }
        }

        return savedAttachmentUrls;
    }

    @Override
    public void createAttachments(List<Attachment> attachments) {
        List<AttachmentJpaEntity> attachmentJpaEntities = new ArrayList<>();
        for (Attachment attachment : attachments) {
            PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByIdAndStatus(attachment.getPostId(), BaseStatusType.ACTIVE)
                    .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

            attachmentJpaEntities.add(attachmentMapper.toJpaEntity(postBaseJpaEntity, attachment));
        }

        attachmentRepository.saveAll(attachmentJpaEntities);
    }

    @Override
    public void deleteAllAttachments(List<Attachment> attachments) {
        // 1. 삭제할 파일이 없으면 그대로 종료
        if (attachments.isEmpty()) {
            return; // 삭제할 파일이 없으면 그대로 종료
        }

        // 2. S3에서 파일 삭제
        for (Attachment attachment : attachments) {
            s3Uploader.deleteFileByUrl(attachment.getAttachmentUrl()); // S3 에서 삭제
        }

        // 3. DB에서 Soft Delete 수행
        List<Long> attachmentIds = attachments.stream()
                .map(Attachment::getId)
                .toList();

        List<AttachmentJpaEntity> attachmentJpaEntities = attachmentRepository.findAllById(attachmentIds);

        for (AttachmentJpaEntity attachment : attachmentJpaEntities) {
            attachment.softDelete();
        }
    }
}
