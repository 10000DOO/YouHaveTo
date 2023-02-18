package com.yht.exerciseassist.domain.media.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/jeong-yunju/Documents/wallpaper/" + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        List<Media> mediaList = mediaService.uploadImageToFileSystem(mediaFileList);
        //then
        assertThat(mediaList.get(0).getOriginalFilename()).isEqualTo(media.getOriginalFilename());
    }

    @Test
    public void getMediaFileTest() throws IOException {
        //given
        Media media = Media.builder()
                .id(1L)
                .originalFilename("tuxCoding.jpg")
                .filename("test1.png")
                .filePath("/Users/jeong-yunju/Documents/wallpaper/" + "test1.png")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        FileSystemResource fileSystemResource = new FileSystemResource(media.getFilePath());

        Optional<Media> opMedia = Optional.of(media);
        when(mediaRepository.findById(1L)).thenReturn(opMedia);

        //when
        ResponseEntity result = mediaService.getMediaFile(1L);

        //then
        assertThat(result.getBody()).isEqualTo(fileSystemResource);
    }
}
