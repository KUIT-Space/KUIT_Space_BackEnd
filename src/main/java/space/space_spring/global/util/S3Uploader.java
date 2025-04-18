package space.space_spring.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.validator.AllowedDocumentFileExtensions;
import space.space_spring.global.validator.AllowedImageFileExtensions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BASE64_CONVERT_FAIL_IN_MEMORY;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile이 지원하는 이미지 파일 형식인지 검증
    public boolean isImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        log.info("extension : {}", extension);

        return AllowedImageFileExtensions.contains(extension);
    }

    // MultipartFile이 지원하는 문서 파일 형식인지 검증
    public boolean isDocumentFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        log.info("extension : {}", extension);

        return AllowedDocumentFileExtensions.contains(extension);
    }

    public void deleteFileByUrl(String fileUrl) {
        // 예시: https://bucket.s3.amazonaws.com/dirName/profile.xxx
        // S3 URL 형식에 따라 객체 키를 추출하는 로직
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();        // 예시: /dirName/profile.xxx
            String key = path.startsWith("/") ? path.substring(1) : path;       // path에서 key 파싱

            if (amazonS3Client.doesObjectExist(bucket, key)) {
                amazonS3Client.deleteObject(bucket, key);
                log.info("S3에서 파일 삭제됨 : {}", key);
            } else {
                log.warn("삭제할 파일이 존재하지 않음 : {}", key);
            }
        } catch (MalformedURLException e) {
            log.error("URL 파싱 오류 : {}", fileUrl, e);
        }
    }

    // File에 저장하지 않고 Memory에서 변환 시행
    public String upload(MultipartFile file, String dirName) throws IOException{
        String originFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originFileName);
        String uniqueFilename = generateUniqueFileName(dirName,fileExtension);

        //String fileName = dirName + "/" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            //amazonS3Client.putObject(new PutObjectRequest(bucket, uniqueFilename, inputStream, metadata));
            putS3( uniqueFilename, inputStream, metadata);
            log.info("File uploaded successfully: {}", uniqueFilename);
            return amazonS3Client.getUrl(bucket, uniqueFilename).toString();
        } catch (IOException e) {
            log.error("Error uploading file: {}", uniqueFilename, e);
            throw new CustomException(MULTIPARTFILE_CONVERT_FAIL_IN_MEMORY,"Failed to upload file");
        }
    }

    private String generateUniqueFileName(String dirName ,String fileExtension){
        return dirName + "/" +UUID.randomUUID().toString() + fileExtension;
    }

    // 업로드하기
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String putS3(String fileName,InputStream uploadFile, ObjectMetadata metadata) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile,metadata)
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

    //MultipartFile을 전달 받아 File로 전환 후 S3 업로드
//    public String uploadFile(MultipartFile multipartFile, String dirName) throws IOException{// dirName의 디렉토리가 S3 Bucket 내부에 생성됨
//
//        File uploadFile = convert(multipartFile).orElseThrow(()-> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
//        //System.out.p
//        // print("error: multipart file input. cant control");
//        return upload(uploadFile,dirName);
//    }

//    public String upload(File uploadFile, String dirName){
//        String fileName = dirName+"/"+uploadFile.getName();
//        String uploadImageUrl = putS3(uploadFile,fileName);
//
//        removeNewFile(uploadFile);// convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
//        return uploadImageUrl;
//    }

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

    public String uploadBase64File(String base64File, String dirName, String fileName) throws IOException {
        String[] base64Components = base64File.split(","); // ","을 기준으로 바이트 코드를 나눠준다
        byte[] decodedBytes;
        String fileExtension = "";

        if (base64Components.length > 1) {
            // TODO 1: base64 디코딩
            decodedBytes = Base64.getDecoder().decode(base64Components[1]);

            // TODO 2: 확장자 설정
            fileExtension = getFileExtension(base64Components);
        } else {
            log.error("No prefix found");
            decodedBytes = Base64.getDecoder().decode(base64File);
        }

        // TODO 3: S3에 업로드될 고유한 파일명 설정
        String decodedFileName = "upload_" + UUID.randomUUID() + "_" + fileName + "_" +fileExtension;
        String originalFileName = dirName + "/" + decodedFileName;

        // TODO 4: 임시 리소스 생성
        File uploadFile = new File(System.getProperty("java.io.tmpdir"), decodedFileName);
        try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            log.error("Error writing bytes to file: {}", e.getMessage());
            throw new CustomException(BASE64_CONVERT_FAIL_IN_MEMORY,"Failed to upload file");
        }

        // TODO 5: S3에 파일 업로드 및 임시 리소스 삭제
        String uploadImageUrl = putS3(uploadFile, originalFileName);
        uploadFile.delete();

        return uploadImageUrl;
    }

    private String getFileExtension(String[] base64Components) {
        String filePrefix = base64Components[0].split(";")[0].split(":")[1];
        return switch (filePrefix) {
            case "image/jpeg" -> ".jpeg";
            case "image/png" -> ".png";
            case "image/jpg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "image/svg+xml" -> ".svg";
            case "image/bmp" -> ".bmp";
            case "image/tif" -> ".tif";
            case "image/tiff" -> ".tiff";
            case "image/heic" -> ".heic";
            case "application/pdf" -> ".pdf";
            case "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx";
            case "application/vnd.hwp" -> ".hwp";
            case "text/plain" -> ".txt";
            default -> {
                log.info("Unsupported file prefix: " + filePrefix);
                yield ".bin"; // 기본 확장자
            }
        };
    }

    private String getFileExtension(String fileName){
        if(fileName==null){
            return "";
        }
        int lastIndexOf = fileName.lastIndexOf(".");
        if(lastIndexOf==-1){
            return "";
        }
        return fileName.substring(lastIndexOf);
    }
}
