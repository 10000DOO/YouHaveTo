package com.yht.exerciseassist.domain.media.service;

import com.yht.exerciseassist.domain.factory.MediaFactory;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
@ActiveProfiles("test")
class MediaServiceTest {

    private MediaService mediaService;

    @Value("${file.dir}")
    private String fileDir;
    @Value("${test.address}")
    private String testAddress;

    @MockBean
    private MediaRepository mediaRepository;

    @BeforeEach
    void setUp() {
        mediaService = new MediaService(mediaRepository);
    }

    @Test
    public void imageUpload() throws IOException {
        //given
        Media media = MediaFactory.createTeatMedia(testAddress + "tuxCoding.jpg");

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        List<Media> mediaList = mediaService.uploadMediaToFiles(mediaFileList);
        File file = new File(mediaList.get(0).getFilePath());
        file.delete();
        //then
        assertThat(mediaList.get(0).getOriginalFilename()).isEqualTo(media.getOriginalFilename());
    }

    @Test
    public void getMediaFileTest() throws IOException {
        //given
        Media media = MediaFactory.createTeatMedia(testAddress + "test1.png");

        media.setMediaIdUsedOnlyTest(1L);

        FileSystemResource fileSystemResource = new FileSystemResource(media.getFilePath());

        Optional<Media> opMedia = Optional.of(media);
        when(mediaRepository.findByNotDeletedId(1L)).thenReturn(opMedia);

        //when
        ResponseEntity result = mediaService.getMediaFile(1L);

        //then
        assertThat(result.getBody()).isEqualTo(fileSystemResource);
    }

    @Test
    public void deleteFile() throws IOException {
        //given
        Media media = MediaFactory.createTeatMedia(fileDir + "tuxCoding.jpg");
        media.setMediaIdUsedOnlyTest(1L);
        List<Media> mediaList = new ArrayList<Media>();
        mediaList.add(media);

        Mockito.when(mediaRepository.findByNotDeletedDiaryId(1L)).thenReturn(mediaList);

        MockMultipartFile mediaFile = new MockMultipartFile("files", media.getOriginalFilename(), "image/jpeg", new FileInputStream(testAddress + "tuxCoding.jpg"));
        mediaFile.transferTo(new File(fileDir + media.getOriginalFilename()));
        //when
        mediaService.deleteDiaryMedia(1L);
        //then
    }
}
