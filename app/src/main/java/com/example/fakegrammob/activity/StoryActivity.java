package com.example.fakegrammob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.PostDto;
import com.example.fakegrammob.dto.PostPageDto;

import java.util.ArrayList;
import java.util.List;

import static com.example.fakegrammob.facade.ClaimRetrieve.retrieveUserIdFromToken;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getContent;

public class StoryActivity extends AppCompatActivity implements MainAdapter.OnItemListener {
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<PostDto> posts = new ArrayList<>();
    private MainAdapter mainAdapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        //init adapter
        final long authorId = retrieveUserIdFromToken(getApplicationContext());
        final PostPageDto postPageDto = getContent(page, progressBar, authorId);
        if (postPageDto != null) {
            posts=postPageDto.getPosts();
        }
        mainAdapter = new MainAdapter(posts, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                final PostPageDto content = getContent(page, progressBar, authorId);
                mainAdapter=new MainAdapter(content.getPosts(),this,this);
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
}