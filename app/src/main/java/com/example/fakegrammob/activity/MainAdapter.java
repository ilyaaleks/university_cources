package com.example.fakegrammob.activity;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;
import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.HashTagDto;
import com.example.fakegrammob.dto.MarkDto;
import com.example.fakegrammob.dto.PostDto;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;
import static com.example.fakegrammob.facade.ServerEndpointsFacade.getCountOfLike;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<PostDto> posts;
    private Activity activity;
    private OnItemListener mOnItemListener;

    public MainAdapter(List<PostDto> posts, Activity activity, OnItemListener onItemListener) {
        this.posts = posts;
        this.activity = activity;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view, mOnItemListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String serverUrl = BASE_URL + "img/";
        final PostDto postDto = posts.get(position);
        //with glide maybe, 11.03 in video https://www.youtube.com/watch?v=9r-BoGoZWVs
        holder.tweetImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        holder.tweetImageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        holder.tweetImageView.setImageUrl(serverUrl + postDto.getPhotoPath());
        holder.userImageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        holder.userImageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        holder.userImageView.setImageUrl(serverUrl + postDto.getAuthorPhotoPath());
        holder.nameTextView.setText(postDto.getAuthorLogin());
        holder.creationDateTextView.setText(postDto.getDate().toString());
        holder.contentTextView.setText(postDto.getText());
        String hashTags = "";
        if (postDto != null && postDto.getHashTags() != null) {
            hashTags = postDto.getHashTags().stream().map(HashTagDto::getText).collect(Collectors.joining(", "));
        }
        holder.nickTextView.setText(hashTags);
        final MarkDto postMark = getCountOfLike(postDto.getId(), postDto.getAuthorId());
        holder.dislikesTextView.setText(String.valueOf(postMark.getCountOfDislikes()));
        holder.likesTextView.setText(String.valueOf(postMark.getCountOfLikes()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ANImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;
        private TextView creationDateTextView;
        private TextView contentTextView;
        private ANImageView tweetImageView;
        private TextView dislikesTextView;
        private TextView likesTextView;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.author_name_text_view);
            nickTextView = itemView.findViewById(R.id.author_nick_text_view);
            creationDateTextView = itemView.findViewById(R.id.creation_date_text_view);
            contentTextView = itemView.findViewById(R.id.tweet_content_text_view);
            tweetImageView = itemView.findViewById(R.id.tweet_image_view);
            dislikesTextView = itemView.findViewById(R.id.count_dislikes_text_view);
            likesTextView = itemView.findViewById(R.id.count_likes_text_view);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
