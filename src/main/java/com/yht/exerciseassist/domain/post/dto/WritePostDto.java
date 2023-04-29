package com.yht.exerciseassist.domain.post.dto;

import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class WritePostDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자까지 입력할 수 있습니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 1000, message = "본문은 1000자까지 입력할 수 있습니다.")
    private String content;

    @NotNull(message = "게시글 카테고리를 선택해주세요.")
    private PostType postType;

    @NotNull(message = "운동 종류를 선택해주세요.")
    private WorkOutCategory workOutCategory;

    @Builder
    public WritePostDto(String title, String content, PostType postType, WorkOutCategory workOutCategory) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
    }
}
