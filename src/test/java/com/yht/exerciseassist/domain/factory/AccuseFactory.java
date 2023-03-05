package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.accuse.AccuseType;
import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.post.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccuseFactory {

    public static Accuse createAccuse(Post post) {

        return Accuse.builder()
                .accuseType(AccuseType.ABUSIVE_LANGUAGE_BELITTLE)
                .content("욕설")
                .post(post)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();
    }

    public static AccuseReq createAccuseReq() {

        return new AccuseReq(AccuseType.ABUSIVE_LANGUAGE_BELITTLE, "욕설");
    }
}
