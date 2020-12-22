package com.example.fakegrammob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;
import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.PostDto;
import com.example.fakegrammob.dto.PostPageDto;
import com.example.fakegrammob.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static com.example.fakegrammob.facade.ClaimRetrieve.retrieveUserIdFromToken;
import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getUserById;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getUserPosts;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.subscribeOnUser;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.unsubscribeFromUser;

public class UserActivity extends AppCompatActivity implements MainAdapter.OnItemListener {
    private NestedScrollView nestedScrollView;
    private ANImageView imageView;
    private TextView usernameView;
    private TextView userDescriptionView;
    private RecyclerView recyclerView;
    private List<PostDto> posts = new ArrayList<>();
    private MainAdapter mainAdapter;
    private int page = 0;
    private Button subscribeButton;
    private UserDto user;
    private Button settingsButton;
    private final String serverUrl = BASE_URL + "img/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        recyclerView = findViewById(R.id.recycler_view);
        imageView = findViewById(R.id.userAvatarView);
        usernameView = findViewById(R.id.userDetatil);
        userDescriptionView = findViewById(R.id.userDescription);
        settingsButton = findViewById(R.id.settings_button);
        final long userId = getIntent().getExtras().getLong("userId");
        final PostPageDto postPageDto = getUserPosts(page, userId);
        if (postPageDto != null) {
            posts = postPageDto.getPosts();
        }
        user = getUserById(userId);
        usernameView.setText(user.getUsername());
        userDescriptionView.setText(user.getUserDescription());
        imageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        imageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        imageView.setImageUrl(serverUrl + user.getPhotoUrl());
        subscribeButton = findViewById(R.id.subscribe_unsubscribe_button);
        if (user.getId() == retrieveUserIdFromToken(getApplicationContext())) {
            subscribeButton.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
        }
        if (user.isSubscribed()) {
            subscribeButton.setText("unsubscribe");
        }
        mainAdapter = new MainAdapter(posts, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                final PostPageDto content = getUserPosts(page, userId);
                mainAdapter = new MainAdapter(content.getPosts(), this, this);
                recyclerView.setAdapter(mainAdapter);
            }
        });
    }

    public void subscribeOnUserEvent(View view) {
        if (user.isSubscribed()) {
            user = unsubscribeFromUser(user.getId());
            subscribeButton.setText("subscribe");
        } else {
            user = subscribeOnUser(user.getId());
            subscribeButton.setText("unsubscribe");
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PostActivity.class);
        final PostDto post = posts.get(position);
        intent.putExtra("postId", post.getId());
        startActivity(intent);
    }

    public void updateUser(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("updateFlag", true);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}