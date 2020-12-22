package com.example.fakegrammob.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private long id;
    @NonNull
    private String lastName;
    @NonNull
    private String name;
    @NonNull
    private String username;
    private String password;
    @NonNull
    private String email;
    @NonNull
    private String userDescription;
    @NonNull
    private Boolean activate;
    @NonNull
    private String status;
    private String photoUrl;
    private int countOfSubscribers;
    private int countOfSubscriptions;
    private int countOfPosts;
    private boolean subscribed;
}
