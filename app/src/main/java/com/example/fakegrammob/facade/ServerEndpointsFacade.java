package com.example.fakegrammob.facade;


import android.view.View;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.example.fakegrammob.dto.AuthToken;
import com.example.fakegrammob.dto.CommentDto;
import com.example.fakegrammob.dto.CommentPageDto;
import com.example.fakegrammob.dto.LoginPasswordUser;
import com.example.fakegrammob.dto.MarkDto;
import com.example.fakegrammob.dto.PostDto;
import com.example.fakegrammob.dto.PostPageDto;
import com.example.fakegrammob.dto.UserDto;

import java.io.File;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.example.fakegrammob.facade.ServerEndpoints.ADD_POST_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.AUTH_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.GET_POST_BY_ID_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.POST_COMMENTS_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.REGISTRATION_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.SAVE_LIKE_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.SAVE_POST_COMMENTS_URL;
import static com.example.fakegrammob.facade.ServerEndpoints.SUBSCRIPTION_POSTS_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerEndpointsFacade {
    public static AuthToken doLogin(String username, String password) {
        LoginPasswordUser user = new LoginPasswordUser(username, password);
        final ANResponse<AuthToken> response = AndroidNetworking.post(AUTH_URL)
                .addApplicationJsonBody(user)
                .setPriority(Priority.MEDIUM)
                .build().executeForObject(AuthToken.class);
        return response.getResult();
    }

    public static AuthToken doRegistration(final UserDto user) {
        final ANResponse<AuthToken> response = AndroidNetworking.post(REGISTRATION_URL)
                .addApplicationJsonBody(user)
                .setPriority(Priority.MEDIUM)
                .build().executeForObject(AuthToken.class);
        return response.getResult();
    }

    public static PostPageDto getContent(final Integer page, final ProgressBar progressBar, final Long userId) {
        final ANResponse<PostPageDto> response = AndroidNetworking.get(SUBSCRIPTION_POSTS_URL.replace("{page}", page.toString()).replace("{userId}", userId.toString()))
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .executeForObject(PostPageDto.class);
        progressBar.setVisibility(View.GONE);
        return response.getResult();
    }

    public static PostDto savePost(final File file, final long authorId, final String hashTags, final String text) {
        final ANResponse<PostDto> response = AndroidNetworking.upload(ADD_POST_URL)
                .addMultipartFile("file", file)
                .addMultipartParameter("authorId", String.valueOf(authorId))
                .addMultipartParameter("hashTags", hashTags)
                .addMultipartParameter("text", text)
                .setPriority(Priority.HIGH)
                .build()
                .executeForObject(PostDto.class);
        return response.getResult();
    }

    public static PostDto getPostById(final Long id) {
        final ANResponse<PostDto> response = AndroidNetworking.get(GET_POST_BY_ID_URL.replace("{id}", id.toString()))
                .setPriority(Priority.MEDIUM)
                .build().executeForObject(PostDto.class);
        return response.getResult();
    }

    public static CommentPageDto getPostComments(final Integer page, final Long postId) {
        final ANResponse<CommentPageDto> response = AndroidNetworking.get(POST_COMMENTS_URL.replace("{page}", page.toString()).replace("{postId}", postId.toString()))
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .executeForObject(CommentPageDto.class);
        return response.getResult();
    }

    public static CommentDto saveComment(CommentDto commentDto) {
        final ANResponse<CommentDto> response = AndroidNetworking.post(SAVE_POST_COMMENTS_URL)
                .addApplicationJsonBody(commentDto)
                .setPriority(Priority.MEDIUM)
                .build().executeForObject(CommentDto.class);
        return response.getResult();
    }

    public static MarkDto saveLike(MarkDto markDto) {
        final ANResponse<MarkDto> response = AndroidNetworking.post(SAVE_LIKE_URL)
                .addApplicationJsonBody(markDto)
                .setPriority(Priority.MEDIUM)
                .build().executeForObject(MarkDto.class);
        return response.getResult();
    }
}
