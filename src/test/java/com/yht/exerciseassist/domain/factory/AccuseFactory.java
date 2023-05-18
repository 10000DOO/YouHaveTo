package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.accuse.AccuseGetType;
import com.yht.exerciseassist.domain.accuse.AccuseType;
import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.post.Post;

public class AccuseFactory {

    public static Accuse createPostAccuse(Post post) {

        return Accuse.builder()
                .accuseType(AccuseType.ABUSIVE_LANGUAGE_BELITTLE)
                .accuseGetType(AccuseGetType.POST)
                .content("욕설")
                .post(post)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();
    }

    public static Accuse createCommentAccuse(Comment comment) {

        return Accuse.builder()
                .accuseType(AccuseType.ABUSIVE_LANGUAGE_BELITTLE)
                .accuseGetType(AccuseGetType.COMMENT)
                .content("욕설")
                .comment(comment)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();
    }

    public static AccuseReq createAccuseReq() {

        return new AccuseReq(AccuseType.ABUSIVE_LANGUAGE_BELITTLE, "욕설");
    }
}
