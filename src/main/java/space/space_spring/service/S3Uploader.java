package space.space_spring.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.exception.CustomException;
import space.space_spring.validator.AllowedImageFileExtensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.MULTIPARTFILE_CONVERT_FAILE_IN_MEMORY;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //MultipartFile을 전달 받아 File로 전환 후 S3 업로드
//    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException{// dirName의 디렉토리가 S3 Bucket 내부에 생성됨
//
//        File uploadFile = convert(multipartFile).orElseThrow(()-> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
//        //System.out.p
//        // print("error: multipart file input. cant control");
//        return upload(uploadFile,dirName);
//    }

    // File에 저장하지 않고 Memory에서 변환 시행
    public String upload(MultipartFile file, String dirName) throws IOException{
        String fileName = dirName + "/" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
            log.info("File uploaded successfully: {}", fileName);
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            log.error("Error uploading file: {}", fileName, e);
            throw new CustomException(MULTIPARTFILE_CONVERT_FAILE_IN_MEMORY,"Failed to upload file");
        }
    }


//    public String upload(File uploadFile, String dirName){
//        String fileName = dirName+"/"+uploadFile.getName();
//        String uploadImageUrl = putS3(uploadFile,fileName);
//
//        removeNewFile(uploadFile);// convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
//        return uploadImageUrl;
//    }
    // 업로드하기
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    // 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }
    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename()); // 업로드한 파일의 이름
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // MultipartFile이 지원하는 이미지 파일 형식인지 검증
    public boolean isFileImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        log.info("extension : {}", extension);

        return AllowedImageFileExtensions.contains(extension);
    }
}
