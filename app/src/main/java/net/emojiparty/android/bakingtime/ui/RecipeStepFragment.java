package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import net.emojiparty.android.bakingtime.BR;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Step;
import net.emojiparty.android.bakingtime.data.StepPresenter;

public class RecipeStepFragment extends Fragment {
  private RecipeDetailViewModel detailViewModel;
  private View root;
  private SimpleExoPlayerView playerView;
  private SimpleExoPlayer exoPlayer;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    ViewDataBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
    root = binding.getRoot();
    playerView = root.findViewById(R.id.video_player);
    setupViewModel(binding);
    return root;
  }

  // https://stackoverflow.com/questions/45481775/exoplayer-restore-state-when-resumed
  @Override public void onResume() {
    super.onResume();
    Long lastPlayedVideoPosition = detailViewModel.getLastPlayedVideoPosition().getValue();
    if (lastPlayedVideoPosition != null && lastPlayedVideoPosition != C.TIME_UNSET && exoPlayer != null) {
      exoPlayer.seekTo(lastPlayedVideoPosition);
    }
  }

  @Override public void onPause() {
    super.onPause();
    if (exoPlayer != null) {
      detailViewModel.getLastPlayedVideoPosition().setValue(exoPlayer.getCurrentPosition());
      exoPlayer.stop();
    }
  }

  private void setupViewModel(final ViewDataBinding binding) {
    FragmentActivity activity = getActivity();
    detailViewModel = ViewModelProviders.of(activity).get(RecipeDetailViewModel.class);
    binding.setLifecycleOwner(activity);
    final StepPresenter.OnStepChanged onStepChanged = new StepPresenter.OnStepChanged() {
      @Override public void callback() {
        if (exoPlayer != null) {
          exoPlayer.stop();
        }
      }
    };
    detailViewModel.getSelectedStep().observe(activity, new Observer<Step>() {
      @Override public void onChanged(@Nullable Step step) {
        StepPresenter stepPresenter = new StepPresenter(detailViewModel, onStepChanged);
        binding.setVariable(BR.presenter, stepPresenter);
        if (step.getVideoURL() != null && !step.getVideoURL().equals("")) {
          initializePlayer(step);
        }
      }
    });
  }

  private void initializePlayer(Step step) {
    Context context = getContext();
    String userAgent = Util.getUserAgent(context, "BakingTime");

    // only need to do this once per fragment
    if (exoPlayer == null) {
      exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(),
          new DefaultLoadControl());
      playerView.setPlayer(exoPlayer);
    }

    // but this needs to be refreshed with the new videoUri when the step has changed
    Uri videoUri = Uri.parse(step.getVideoURL());
    MediaSource mediaSource =
        new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(context, userAgent),
            new DefaultExtractorsFactory(), null, null);
    exoPlayer.setPlayWhenReady(true);
    exoPlayer.prepare(mediaSource);
  }

  private void releasePlayer() {
    if (exoPlayer != null) {
      exoPlayer.release();
      exoPlayer = null;
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    releasePlayer();
  }
}
