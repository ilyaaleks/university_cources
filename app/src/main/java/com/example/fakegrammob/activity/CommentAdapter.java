package com.example.fakegrammob.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.widget.ANImageView;
import com.example.fakegrammob.R;
import com.example.fakegrammob.dto.CommentDto;

import java.util.List;

import static com.example.fakegrammob.facade.ServerEndpoints.BASE_URL;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private List<CommentDto> comments;
    private Activity activity;
    private CommentAdapter.OnItemListener mOnItemListener;

    public CommentAdapter(List<CommentDto> comments, Activity activity, CommentAdapter.OnItemListener onItemListener) {
        this.comments = comments;
        this.activity = activity;
        this.mOnItemListener=onItemListener;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_comment, parent, false);
        return new CommentAdapter.ViewHolder(view,mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        final String serverUrl=BASE_URL+"img/";
        final CommentDto commentDto = comments.get(position);
        //with glide maybe, 11.03 in video https://www.youtube.com/watch?v=9r-BoGoZWVs
        holder.imageView.setDefaultImageResId(R.drawable.ic_launcher_foreground);
        holder.imageView.setErrorImageResId(R.drawable.ic_baseline_error_24);
        holder.imageView.setImageUrl(serverUrl+commentDto.getAuthorPhotoPath());

        holder.textView.setText(commentDto.getText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ANImageView imageView;
        TextView textView;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
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
