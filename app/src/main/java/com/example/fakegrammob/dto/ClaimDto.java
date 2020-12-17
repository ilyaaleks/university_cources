package com.example.fakegrammob.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClaimDto {
    private long id;
    private long postId;
    private Date date;
    private String reason;
    private String status;
    private String authorId;
}
