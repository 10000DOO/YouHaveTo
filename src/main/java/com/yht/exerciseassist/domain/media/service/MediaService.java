package com.yht.exerciseassist.domain.media.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import com.yht.exerciseassist.exceoption.error.ErrorCode;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${file.dir}")
    private String fileDir;

    public List<Media> uploadImageToFileSystem(List<MultipartFile> files) throws IOException {

        List<Media> mediaList = new ArrayList<>();

        for (MultipartFile file : files) {
            String storeFileName = createStoreFileName(file.getOriginalFilename());

            Media media = Media.builder()
                    .originalFilename(file.getOriginalFilename())
                    .filename(storeFileName)
                    .filePath(fileDir + storeFileName)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            mediaList.add(media);
            file.transferTo(new File(fileDir + storeFileName));
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
        Optional<Media> findMedia = mediaRepository.findById(mediaId);

        Media media = findMedia.orElseThrow(() -> new EntityNotFoundException(ErrorCode.NotFoundExceptionMedia.getMessage()));
        HttpHeaders header = new HttpHeaders();

        FileSystemResource fileSystemResource = new FileSystemResource(media.getFilePath());
        Path filePath = Paths.get(media.getFilePath());
        header.add("Content-Type", Files.probeContentType(filePath));
        log.info(media.getFilePath() + "출력 성공");

        return ResponseEntity.status(HttpStatus.OK).headers(header).body(fileSystemResource);
    }

    public void deleteFile(Long diaryId) throws IOException {
        List<Media> byDiaryId = mediaRepository.findByDiaryId(diaryId);

        if (byDiaryId != null && !byDiaryId.isEmpty()) {
            for (Media media : byDiaryId) {
                File file = new File(media.getFilePath());
                boolean deleteSuccess = file.delete();
                mediaRepository.deleteById(media.getId());
                if (!deleteSuccess) {
                    throw new IOException(ErrorCode.DeleteFailedMediaException.getMessage());
                }
                log.info(media.getFilename() + " 삭제 완료");
            }
        }
    }
}
