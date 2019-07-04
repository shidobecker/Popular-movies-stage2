package com.example.android.popular_movies_stage2.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popular_movies_stage2.R;
import com.example.android.popular_movies_stage2.databinding.ActivityVideosListBinding;
import com.example.android.popular_movies_stage2.model.domain.Video;
import com.example.android.popular_movies_stage2.utils.VideoWebsite;
import com.example.android.popular_movies_stage2.viewmodel.VideoListViewModel;
import com.example.android.popular_movies_stage2.viewmodel.VideoListViewModelFactory;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class VideosListActivity extends AppCompatActivity implements VideoAdapter.VideoClickListener {

    private String apiKey;

    private int movieId;

    private ActivityVideosListBinding binding;

    private VideoListViewModel viewModel;

    private VideoAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_videos_list);

        if (getIntent().hasExtra(DetailsActivity.MOVIE_ID_EXTRA)) {
            movieId = getIntent().getIntExtra(DetailsActivity.MOVIE_ID_EXTRA, 0);
            String movieName = getIntent().getStringExtra(DetailsActivity.MOVIE_NAME_EXTRA);
            apiKey = getString(R.string.API_KEY_TMDB);

            getSupportActionBar().setTitle(movieName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setupViewModel();

            setupAdapter();

            getLifecycle().addObserver(binding.youtubePlayerView);

        } else {
            Toast.makeText(this, getString(R.string.toast_message_intent_error), Toast.LENGTH_LONG).show();
            finish();
        }


    }

    private void setupViewModel() {
        VideoListViewModelFactory factory = new VideoListViewModelFactory(movieId, apiKey);
        viewModel = ViewModelProviders.of(this, factory).get(VideoListViewModel.class);
    }

    private void setupAdapter() {
        adapter = new VideoAdapter(this);

        binding.videosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.videosRecyclerView.setAdapter(adapter);

        viewModel.getObservableVideos().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(@Nullable List<Video> videos) {
                if (videos != null) {
                    handleVideosSuccess(videos);
                } else {
                    handleVideosError();
                }
            }
        });
    }

    private void handleVideosSuccess(final List<Video> videos) {
        binding.videosProgressBar.setVisibility(View.GONE);

        if (videos.isEmpty()) {
            binding.videosErrorMessage.setVisibility(View.VISIBLE);
            binding.videosErrorMessage.setText(R.string.videos_no_videos_found);
            binding.videosRecyclerView.setVisibility(View.GONE);

            binding.youtubePlayerView.setVisibility(View.GONE);
            binding.youtubePlayerView.release();
        } else {
            binding.videosErrorMessage.setVisibility(View.GONE);
            binding.videosRecyclerView.setVisibility(View.VISIBLE);

            String videoId = videos.get(0).getKey();
            startYoutubePlayerListener(videoId);

        }
        adapter.setVideoList(videos);
    }

    private void startYoutubePlayerListener(final String youtubeVideoId) {
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(youtubeVideoId, 0);
            }
        });
    }

    private void handleVideosError() {
        binding.videosProgressBar.setVisibility(View.GONE);
        binding.videosErrorMessage.setVisibility(View.VISIBLE);
        binding.videosRecyclerView.setVisibility(View.GONE);

        binding.videosErrorMessage.setText(R.string.videos_list_connection_message);

        binding.youtubePlayerView.setVisibility(View.GONE);
        binding.youtubePlayerView.release();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.youtubePlayerView.release();

    }

    @Override
    public void onClickVideoPlay(String videoKey, String webSite) {
        Intent appIntent;

        Intent webIntent;

        if (webSite.equalsIgnoreCase(VideoWebsite.YOUTUBE.name())) {
            appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_base_app) + videoKey));
            webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_base_url) + videoKey));
        } else {
            appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vimeo_base_app) + videoKey));
            webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vimeo_base_url) + videoKey));
        }

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }

    }

    @Override
    public void onClickShare(Video video) {
        String mimeType = "text/plain";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(video.getName())
                .setText(getString(R.string.youtube_base_url) + video.getKey())
                .startChooser();
    }
}
