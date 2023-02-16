package com.yht.exerciseassist.domain.media.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@Rollback
class MediaServiceTest {

    private MediaService mediaService;

    @MockBean
    private MediaRepository mediaRepository;

    @BeforeEach
    void setUp() {
        mediaService = new MediaService(mediaRepository);
    }

    @Test
    public void imageUpload() throws IOException {
        //given
        Media media = Media.builder()
                .originalFilename("tuxCoding.jpg")
                .filename("storeFileName.jpg")
                .filePath("/Users/10000doo/Documents/wallpaper/" + "storeFileName.jpg")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);

        when(mediaRepository.save(media)).thenReturn(media);
        //when
        mediaService.uploadImageToFileSystem(mediaFileList);
        //then

    }
}
