package com.example.fakegrammob.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageDto {
    private List<CommentDto> comments;
    private int currentPage;
    private int totalPage;
    private long countOfComments;
}
