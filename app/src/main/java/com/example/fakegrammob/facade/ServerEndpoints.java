package com.example.fakegrammob.facade;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerEndpoints {
    public static final String BASE_URL = "http://10.0.2.2:9000/";
    public static final String AUTH_URL = BASE_URL + "api/auth/token";
    public static final String REGISTRATION_URL = BASE_URL + "api/registration";
    public static final String USER_POSTS_URL = BASE_URL + "api/post/userposts/{userId}?page={page}&size=5&sort=id,DESC";
    public static final String ADD_POST_URL = BASE_URL + "api/post/posts";
    public static final String GET_POST_BY_ID_URL = BASE_URL + "api/post/{id}";
    public static final String POST_COMMENTS_URL = BASE_URL + "api/comment/{postId}?page={page}&size=5&sort=id,DESC";
    public static final String SAVE_POST_COMMENTS_URL = BASE_URL + "api/comment";
    public static final String SAVE_LIKE_URL = BASE_URL + "api/vote/like";
    public static final String SUBSCRIPTION_POSTS_URL = BASE_URL + "api/post/subscriptionPosts/{userId}?page={page}&size=5&sort=id,DESC";
    public static final String COUNT_OF_LIKES_URL = BASE_URL + "api/vote/like/count/{postId}?userId={userId}";
    public static final String USER_BY_ID_URL = BASE_URL + "api/user/{userId}";
    public static final String USER_BY_USERNAME_URL = BASE_URL + "api/user?username={username}";
    public static final String SUBSCRIBE_URL = BASE_URL + "api/user/subscribe?userId={userId}";
    public static final String UNSUBSCRIBE_URL = BASE_URL + "api/user/unsubscribe?userId={userId}";
}
