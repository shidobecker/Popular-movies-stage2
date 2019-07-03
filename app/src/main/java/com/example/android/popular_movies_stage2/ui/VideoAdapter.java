package com.example.android.popular_movies_stage2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.model.domain.Video;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<Video> videoList = new ArrayList<>();

    private VideoClickListener videoClickListener;

    interface VideoClickListener{
        void onClickVideoPlay(String videoKey);

        void onClickShare(Video video);
    }


    VideoAdapter(VideoClickListener clickListener){
        videoClickListener = clickListener;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_item, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        void bind(final Video video) {
            name.setText(video.getName());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClickListener.onClickVideoPlay(video.getKey());
                }
            });
        }

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.video_list_name);
        }
    }

}
