package com.example.fakegrammob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

import static android.text.TextUtils.isEmpty;
import static com.example.fakegrammob.facade.ClaimRetrieve.retrieveUserIdFromToken;
import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getSubscriptionPosts;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getUserById;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getUserByUsername;

public class StoryActivity extends AppCompatActivity implements MainAdapter.OnItemListener {
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<PostDto> posts = new ArrayList<>();
    private MainAdapter mainAdapter;
    private EditText searchEditText;
    private ANImageView imageView;
    private final String serverUrl = BASE_URL + "img/";
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        searchEditText = findViewById(R.id.search_edit_text);
        imageView = findViewById(R.id.userAvatarView);
        //init adapter
        final long authorId = retrieveUserIdFromToken(getApplicationContext());
        final UserDto user = getUserById(authorId);
        imageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        imageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        imageView.setImageUrl(serverUrl + user.getPhotoUrl());
        final PostPageDto postPageDto = getSubscriptionPosts(page, progressBar, authorId);
        if (postPageDto != null) {
            posts = postPageDto.getPosts();
        }
        mainAdapter = new MainAdapter(posts, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                final PostPageDto content = getSubscriptionPosts(page, progressBar, authorId);
                mainAdapter = new MainAdapter(content.getPosts(), this, this);
                recyclerView.setAdapter(mainAdapter);
            }
        });


    }

    public void openAddingActivity(View view) {
        Intent intent = new Intent(this, PostAddingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PostActivity.class);
        final PostDto post = posts.get(position);
        intent.putExtra("postId", post.getId());
        startActivity(intent);
    }

    public void findUserByUsername(View view) {
        final String username = searchEditText.getText().toString();
        if (isEmpty(username)) {
            //message
        }
        final UserDto user = getUserByUsername(username);
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    public void openAboutUserActivity(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("userId", retrieveUserIdFromToken(this));
        startActivity(intent);
    }
}