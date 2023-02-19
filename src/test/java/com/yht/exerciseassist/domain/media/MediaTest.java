package com.yht.exerciseassist.domain.media;

import com.yht.exerciseassist.domain.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")

public class MediaTest {
    @Test
    public void MediaTest() {

        //when
        Media media = Media.builder()
                .originalFilename("tuxCoding.jpg")
                .filename("test1.png")
                .filePath("/Users/jeong-yunju/Documents/wallpaper/" + "test1.png")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        media.setMediaIdUsedOnlyTest(1L);

        //then
        assertThat(media.getId()).isEqualTo(1L);
        assertThat(media.getOriginalFilename()).isEqualTo("tuxCoding.jpg");
        assertThat(media.getFilename()).isEqualTo("test1.png");
        assertThat(media.getFilePath()).isEqualTo("/Users/jeong-yunju/Documents/wallpaper/test1.png");
        assertThat(media.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));

    }
}
