package com.example.shaol.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shaol on 5/19/2018.
 */

public class StepsFragment extends Fragment implements ExoPlayer.EventListener {

    public static final String VIDEO_URL = "video url";
    public static final String THUMBNAIL_URL = "thumbnail url";

    @BindView(R.id.step_description_view)
    TextView stepDescriptionView;
    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;
    @BindView(R.id.playerImageView)
    ImageView playerImageView;

    String stepDescription;
    String stepVideoUrl;
    String stepThumbnailUrl;
    String mURL;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private static final String TAG = StepsFragment.class.getSimpleName();

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_view, container, false);

        ButterKnife.bind(this, rootView);

        stepDescriptionView.setText(stepDescription);

        if (savedInstanceState != null) {
            stepVideoUrl = savedInstanceState.getString(VIDEO_URL);
            stepThumbnailUrl = savedInstanceState.getString(THUMBNAIL_URL);
        }


        if (!stepVideoUrl.equals("")) {
            playerImageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            if (savedInstanceState == null) {initializeMediaSession();}
            mURL = stepVideoUrl;
            if (savedInstanceState == null) {initializePlayer(Uri.parse(mURL));}
        } else if (!stepThumbnailUrl.equals("")) {
            playerImageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            initializeMediaSession();
            mURL = stepThumbnailUrl;
            initializePlayer(Uri.parse(mURL));
        } else {
            playerImageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            mURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Touched_by_His_Noodly_Appendage_HD.jpg/1200px-Touched_by_His_Noodly_Appendage_HD.jpg";
            Uri mUri = Uri.parse(mURL);
            Picasso.get()
                    .load(mUri)
                    .into(playerImageView);
        }
        return rootView;
    }

    public void setSteps(String description, String videoURL, String thumbnailURL) {
        stepDescription = description;
        stepVideoUrl = videoURL;
        stepThumbnailUrl = thumbnailURL;
    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                    PlaybackStateCompat.ACTION_PAUSE |
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious () {
            exoPlayer.seekTo(0);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerView.getVisibility() == View.VISIBLE && exoPlayer != null) {
            releasePlayer();
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(VIDEO_URL, stepVideoUrl);
        currentState.putString(THUMBNAIL_URL, stepThumbnailUrl);
    }
}
