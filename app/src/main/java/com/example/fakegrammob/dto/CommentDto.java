package com.example.fakegrammob.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    private long id;
    @NonNull
    private String text;
    @NonNull
    private Date date;
    @NonNull
    private long postId;
    @NonNull
    private long userId;
    @NonNull
    private String authorPhotoPath;
}
