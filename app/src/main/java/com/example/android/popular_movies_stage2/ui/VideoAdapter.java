package com.example.android.popular_movies_stage2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.model.domain.Video;
import com.example.android.popular_movies_stage2.utils.VideoWebsite;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final VideoClickListener videoClickListener;
    private List<Video> videoList = new ArrayList<>();

    VideoAdapter(VideoClickListener clickListener) {
        videoClickListener = clickListener;
    }

    void setVideoList(List<Video> videoList) {
        //Filtering only videos that has youtube or vimeo as website
        List<Video> filteredVideos = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getSite().equalsIgnoreCase(VideoWebsite.YOUTUBE.name()) ||
                    video.getSite().equalsIgnoreCase(VideoWebsite.VIMEO.name())) {
                filteredVideos.add(video);
            }
        }
        this.videoList = filteredVideos;
        notifyDataSetChanged();
    }

    interface VideoClickListener {
        void onClickVideoPlay(String videoKey, String webSite);

        void onClickShare(Video video);
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

        final TextView name;

        final ImageView logo;

        final ImageView share;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.video_list_name);
            logo = itemView.findViewById(R.id.video_list_logo);
            share = itemView.findViewById(R.id.video_list_share);
        }

        void bind(final Video video) {

            if (video.getSite().equalsIgnoreCase(VideoWebsite.YOUTUBE.name())) {
                logo.setImageResource(R.drawable.ic_youtube_logo_1);
            } else {
                logo.setImageResource(R.drawable.ic_vimeo);
            }

            name.setText(video.getName());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClickListener.onClickVideoPlay(video.getKey(), video.getSite());
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoClickListener.onClickShare(video);
                }
            });
        }
    }

}
