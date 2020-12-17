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
@RequiredArgsConstructor
@NoArgsConstructor
public class MarkDto {
    private long id;
    @NonNull
    private long postId;
    @NonNull
    private long authorId;
    @NonNull
    private TypeOfVote typeOfVote;
    @NonNull
    private Date date;
    private long countOfLikes;
    private long countOfDislikes;
}
