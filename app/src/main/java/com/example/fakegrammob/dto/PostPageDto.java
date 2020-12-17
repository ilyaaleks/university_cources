package com.example.fakegrammob.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PostPageDto {
    private List<PostDto> posts;
    private int currentPage;
    private int totalPage;
}
