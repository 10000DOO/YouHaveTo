package com.yht.exerciseassist.domain.media;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.factory.MediaFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")

public class MediaTest {
    @Value("${test.address}")
    private String testAddress;

    @Test
    public void MediaTest() {
        //given
        //when
        Media media = MediaFactory.createTeatMedia();

        media.setMediaIdUsedOnlyTest(1L);

        //then
        assertThat(media.getId()).isEqualTo(1L);
        assertThat(media.getOriginalFilename()).isEqualTo("tuxCoding.jpg");
        assertThat(media.getFilename()).isEqualTo("test1.png");
        assertThat(media.getFilePath()).isEqualTo("www.amazon-s3.com/tuxCoding.jpg");
        assertThat(media.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));

    }
}
