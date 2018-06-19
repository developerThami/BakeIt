package com.inc.thamsanqa.bakeit;

import android.content.res.Configuration;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    public static final String TAG = "StepDetailFragment";
    public static final String STEP_POSITION = "StepDetailFragment.StepPosition";
    public static final String RECIPE = "StepDetailFragment.Recipe";

    private static final String PLAYER_POSITION = "playerPosition";
    private static final String PLAY_WHEN_READY= "playwhenReady";

    private int position;
    private List<Step> steps;
    private RecipesActivity activity;

    SimpleExoPlayer exoPlayer;

    @BindView(R.id.tv_description)
    TextView mDescription;

    @BindView(R.id.bt_next)
    Button mNext;

    @BindView(R.id.video_player)
    PlayerView mPlayer;

    @BindView(R.id.prev_next_container)
    LinearLayout mPrevAndNextContainer;

    @BindView(R.id.bt_prev)
    Button mPrev;

    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnail;

    private long playbackPosition;
    private boolean playWhenReady;

    private String thumbnailUrl;
    private String videoUrl;

    enum State{
        RESTORED,
        NEW
    }

    static State state = State.NEW;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            state = StepDetailFragment.state.RESTORED;
        }

        if (bundle != null) {
            position = bundle.getInt(STEP_POSITION);
            activity = (RecipesActivity) getActivity();
            Recipe recipe = bundle.getParcelable(RECIPE);
            steps = recipe.getSteps();
            setListeners();
            showStep(position);
            hideButtons();
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putLong(PLAYER_POSITION, playbackPosition);
    }

    private void initialisePlayer(Uri videoUri) {

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                new DefaultTrackSelector());
        mPlayer.setPlayer(exoPlayer);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.app_name))).
                createMediaSource(videoUri);

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(playWhenReady);

        switch (state){
            case NEW:
                exoPlayer.seekTo(0);
                break;
            case RESTORED:
                exoPlayer.seekTo(playbackPosition);
                break;
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void setListeners() {

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position += 1;
                state = State.NEW;
                showStep(position);
            }
        });

        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position -= 1;
                state = State.NEW;
                showStep(position);
            }
        });
    }

    private void showStep(int position) {

        int endPoint = steps.size() - 1;

        if (position <= -1 || position > endPoint) {
            return;
        }

        if (position == 0) {
            hidePreviousButton();
            showNextButton();
            showStepDescription(position);
            return;
        }

        if (position < endPoint) {
            showPreviousButton();
            showNextButton();
            showStepDescription(position);
            return;
        }

        if (position == endPoint) {
            hideNextButton();
            showPreviousButton();
            showStepDescription(position);
        }
    }

    private void hideButtons() {
        if (activity.masterDetailFlow != null) {
            mPrevAndNextContainer.setVisibility(View.GONE);
        }
    }

    private void showStepDescription(int position) {

        adjustForOrientation();

        videoUrl = steps.get(position).getVideoUrl();
        thumbnailUrl = steps.get(position).getThumbnailUrl();

        showVideoThumbnail(thumbnailUrl);

        if (!videoUrl.isEmpty()) {
            showPlayer();
            Uri uri = Uri.parse(videoUrl);
            initialisePlayer(uri);
        } else {
            hidePlayer();
        }

        activity.setTitle(steps.get(position).getShortDescription());
        mDescription.setText(steps.get(position).getDescription());
    }

    private void showVideoThumbnail(String thumbnailUrl) {

        if (!thumbnailUrl.isEmpty()) {
            Picasso.with(getContext())
                    .load(thumbnailUrl)
                    .error(R.drawable.placeholder)
                    .fit()
                    .into(mThumbnail);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.placeholder)
                    .fit()
                    .into(mThumbnail);
        }
    }

    private void hidePlayer() {
        mThumbnail.setVisibility(View.VISIBLE);
        mPlayer.setVisibility(View.GONE);
    }

    private void showPlayer() {
        mThumbnail.setVisibility(View.GONE);
        mPlayer.setVisibility(View.VISIBLE);
    }

    private void hidePreviousButton() {
        mPrev.setVisibility(View.INVISIBLE);
        mPrev.setEnabled(false);
    }

    private void showPreviousButton() {
        mPrev.setVisibility(View.VISIBLE);
        mPrev.setEnabled(true);
    }

    private void hideNextButton() {
        mNext.setVisibility(View.INVISIBLE);
        mNext.setEnabled(false);
    }

    private void showNextButton() {
        mNext.setVisibility(View.VISIBLE);
        mNext.setEnabled(true);
    }

    private void adjustForOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE
                && activity.masterDetailFlow == null) {
            hideSystemUi();
            activity.getSupportActionBar().hide();
        } else {
            activity.getSupportActionBar().show();
        }
    }

    private void hideSystemUi() {
        mPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}
