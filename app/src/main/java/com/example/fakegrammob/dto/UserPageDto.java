package com.example.fakegrammob.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPageDto {
    private List<UserDto> users;
    private int currentPage;
    private int totalPage;
}
