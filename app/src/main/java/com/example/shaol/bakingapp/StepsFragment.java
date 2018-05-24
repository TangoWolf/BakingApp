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

import com.google.android.exoplayer2.C;
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
    public static final String PLAYER_POSITION = "player position";
    public static final String PLAYER_WINDOW = "player window";
    public static final String PLAYER_STATE = "player state";

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
    int window = C.INDEX_UNSET;
    long playerPosition = C.TIME_UNSET;
    boolean playPause = true;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private static final String TAG = StepsFragment.class.getSimpleName();

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_step_view, container, false);

        ButterKnife.bind(this, rootView);

        stepDescriptionView.setText(stepDescription);

        if (savedInstanceState != null) {
            stepVideoUrl = savedInstanceState.getString(VIDEO_URL);
            stepThumbnailUrl = savedInstanceState.getString(THUMBNAIL_URL);
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            window = savedInstanceState.getInt(PLAYER_WINDOW);
            playPause = savedInstanceState.getBoolean(PLAYER_STATE);
        }

        if (!stepVideoUrl.equals("")) {
            playerImageView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            initializeMediaSession();
            mURL = stepVideoUrl;
            initializePlayer(Uri.parse(mURL));
        } else if (!stepThumbnailUrl.equals("")) {
            playerImageView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            String check = stepThumbnailUrl.substring(stepThumbnailUrl.length() - 3);
            if (check.equals("bmp") | check.equals("jpg") | check.equals("png")) {
                mURL = stepThumbnailUrl;
            } else {
                mURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Touched_by_His_Noodly_Appendage_HD.jpg/1200px-Touched_by_His_Noodly_Appendage_HD.jpg";
            }
            Uri mUri = Uri.parse(mURL);
            Picasso.get()
                    .load(mUri)
                    .into(playerImageView);
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
            if (window != C.INDEX_UNSET) {
                exoPlayer.seekTo(window, playerPosition);
            }
            exoPlayer.prepare(mediaSource, false, false);
            exoPlayer.setPlayWhenReady(playPause);
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
    public void onPause() {
        super.onPause();
        if (playerView.getVisibility() == View.VISIBLE && exoPlayer != null) {
            window = exoPlayer.getCurrentWindowIndex();
            playerPosition = exoPlayer.getCurrentPosition();
            playPause = exoPlayer.getPlayWhenReady();
            releasePlayer();
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepVideoUrl != null && !stepVideoUrl.equals("")){
            initializeMediaSession();
            initializePlayer(Uri.parse(stepVideoUrl));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerView.getVisibility() == View.VISIBLE && exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            playPause = exoPlayer.getPlayWhenReady();
            releasePlayer();
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(VIDEO_URL, stepVideoUrl);
        currentState.putString(THUMBNAIL_URL, stepThumbnailUrl);
        currentState.putLong(PLAYER_POSITION, playerPosition);
        currentState.putInt(PLAYER_WINDOW, window);
        currentState.putBoolean(PLAYER_STATE, playPause);
    }
}
