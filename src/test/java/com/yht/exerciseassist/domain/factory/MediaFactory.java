package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;

public class MediaFactory {

    public static Media createTeatMedia() {
        Media media = Media.builder()
                .originalFilename("tuxCoding.jpg")
                .filename("test1.png")
                .filePath("www.amazon-s3.com/tuxCoding.jpg")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        return media;
    }
}
