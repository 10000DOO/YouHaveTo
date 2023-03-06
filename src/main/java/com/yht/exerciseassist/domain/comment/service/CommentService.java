package com.yht.exerciseassist.domain.comment.service;

import com.yht.exerciseassist.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    public ResponseResult<String> saveComment() {
        return null;
    }
}
