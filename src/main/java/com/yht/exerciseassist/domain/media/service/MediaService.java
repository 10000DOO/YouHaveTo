package com.yht.exerciseassist.domain.media.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    @Value("${file.dir}")
    private String fileDir;

    public List<Media> uploadImageToFileSystem(List<MultipartFile> files) throws IOException {

        List<String> filePaths = new ArrayList<>();
        List<Media> mediaList = new ArrayList<>();

        for (MultipartFile file : files) {
            String storeFileName = createStoreFileName(file.getOriginalFilename());
            filePaths.add(fileDir + storeFileName);

            Media media = Media.builder()
                    .originalFilename(file.getOriginalFilename())
                    .filename(storeFileName)
                    .filePath(fileDir + storeFileName)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            mediaList.add(media);
            mediaRepository.save(media);
            file.transferTo(new File(fileDir + storeFileName));
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

    public Resource getMedia(String fullPath) throws MalformedURLException {
        return new UrlResource("file:" + fullPath);
    }
}
