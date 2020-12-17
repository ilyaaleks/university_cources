package com.example.fakegrammob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;
import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.CommentDto;
import com.example.fakegrammob.dto.CommentPageDto;
import com.example.fakegrammob.dto.MarkDto;
import com.example.fakegrammob.dto.PostDto;
import com.example.fakegrammob.dto.TypeOfVote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.fakegrammob.facade.ClaimRetrieve.retrieveUserIdFromToken;
import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getPostById;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getPostComments;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.saveComment;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.saveLike;

public class PostActivity extends AppCompatActivity implements CommentAdapter.OnItemListener {
    private ANImageView picture;
    private TextView postText;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentDto> comments = new ArrayList<>();
    private NestedScrollView nestedScrollView;
    private EditText newCommentText;
    private int page = 0;
    private PostDto currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final long postId = getIntent().getExtras().getLong("postId");
        final PostDto post = getPostById(postId);
        currentPost = post;
        picture = findViewById(R.id.postView);
        postText = findViewById(R.id.postText);
        recyclerView = findViewById(R.id.recycler_view_comments);
        nestedScrollView = findViewById(R.id.scroll_view_comment);
        newCommentText = findViewById(R.id.comment_editor);
        final String serverUrl = BASE_URL + "img/";
        //with glide maybe, 11.03 in video https://www.youtube.com/watch?v=9r-BoGoZWVs
        picture.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        picture.setErrorImageResId(R.drawable.ic_baseline_error_24);
        picture.setImageUrl(serverUrl + post.getPhotoPath());
        //init adapter
        commentAdapter = new CommentAdapter(comments, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        final CommentPageDto postPageDto = getPostComments(page, post.getId());
        comments.addAll(postPageDto.getComments());
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                page++;
                final CommentPageDto content = getPostComments(page, post.getId());
                comments.addAll(content.getComments());
            }
        });
    }

    public void addComment(View view) {
        final long id = retrieveUserIdFromToken(getApplicationContext());
        final String string = newCommentText.getText().toString();
        final CommentDto commentDto = new CommentDto(string, new Date(), currentPost.getId(), id, currentPost.getAuthorPhotoPath());
        final CommentDto comment = saveComment(commentDto);
        comments.add(comment);
    }

    public void setLike(View view) {
        MarkDto markDto = new MarkDto(currentPost.getId(), currentPost.getAuthorId(), TypeOfVote.Like, new Date());
        final MarkDto saveLike = saveLike(markDto);
        printCountOfLikes(saveLike);
    }

    public void setDislike(View view) {
        MarkDto markDto = new MarkDto(currentPost.getId(), currentPost.getAuthorId(), TypeOfVote.Dislike, new Date());
        final MarkDto saveLike = saveLike(markDto);
        printCountOfLikes(saveLike);
    }

    @Override
    public void onItemClick(int position) {

    }

    private void printCountOfLikes(final MarkDto saveLike) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Количество лайков: " + saveLike.getCountOfLikes() + ", количество дизлайков: " + saveLike.getCountOfDislikes(), Toast.LENGTH_LONG);
        toast.show();
    }
}