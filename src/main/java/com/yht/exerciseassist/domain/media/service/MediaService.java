package com.yht.exerciseassist.domain.media.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Media> uploadMediaToFiles(List<MultipartFile> files) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        System.out.println(files.size());

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket, originalFilename, file.getInputStream(), metadata);
            String storeFileName = createStoreFileName(originalFilename);

            Media media = Media.builder()
                    .originalFilename(originalFilename)
                    .filename(storeFileName)
                    .filePath(amazonS3.getUrl(bucket, originalFilename).toString())
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();
            mediaList.add(media);
            mediaRepository.save(media);
            log.info(storeFileName + " 저장되었습니다.");
        }
        return mediaList;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public ResponseEntity<FileSystemResource> getMediaFile(Long mediaId) throws IOException {
        Media findMedia = mediaRepository.findByNotDeletedId(mediaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEDIA.getMessage()));

        FileSystemResource fileSystemResource = new FileSystemResource(findMedia.getFilePath());
        Path filePath = Paths.get(findMedia.getFilePath());

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(filePath));

        log.info(findMedia.getFilePath() + "출력 성공");

        return ResponseEntity.status(HttpStatus.OK).headers(header).body(fileSystemResource);
    }

    public void deleteDiaryMedia(Long diaryId) throws IOException {
        List<Media> byDiaryId = mediaRepository.findByNotDeletedDiaryId(diaryId);

        if (byDiaryId != null && !byDiaryId.isEmpty()) {
            for (Media media : byDiaryId) {
                deleteFile(media);
            }
        }
    }

    public void deletePostMedia(Long postId) throws IOException {
        List<Media> byPostId = mediaRepository.findByNotDeletedPostId(postId);

        if (byPostId != null && !byPostId.isEmpty()) {
            for (Media media : byPostId) {
                deleteFile(media);
            }
        }
    }

    public void deleteProfileImage(Long mediaId) throws IOException {
        Media media = mediaRepository.findByNotDeletedId(mediaId)
                .orElseThrow(() -> new IllegalStateException(ErrorCode.NOT_FOUND_EXCEPTION_MEDIA.getMessage()));
        deleteFile(media);
    }

    private void deleteFile(Media media) throws IOException {
        amazonS3.deleteObject(bucket, media.getOriginalFilename());
        log.info(media.getFilename() + " 삭제 완료");
    }
}
