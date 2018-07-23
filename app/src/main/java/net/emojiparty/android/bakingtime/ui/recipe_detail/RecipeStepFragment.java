package net.emojiparty.android.bakingtime.ui.recipe_detail;

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
import net.emojiparty.android.bakingtime.data.models.Step;

public class RecipeStepFragment extends Fragment {
  private RecipeDetailViewModel detailViewModel;
  private ViewDataBinding binding;
  private SimpleExoPlayerView playerView;
  private SimpleExoPlayer exoPlayer;
  private static final String LAST_PLAYED_POSITION = "LAST_PLAYED_POSITION";
  private long lastPlayedPosition;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
    View root = binding.getRoot();
    playerView = root.findViewById(R.id.video_player);
    if (savedInstanceState != null) {
      lastPlayedPosition = savedInstanceState.getLong(LAST_PLAYED_POSITION, C.TIME_UNSET);
    }
    return root;
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
      setupViewModel(binding);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
      initializePlayer();
      setupViewModel(binding);
    }

    if (lastPlayedPosition != C.TIME_UNSET && exoPlayer != null) {
      exoPlayer.seekTo(lastPlayedPosition);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (exoPlayer != null) {
      outState.putLong(LAST_PLAYED_POSITION, exoPlayer.getCurrentPosition());
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
          setUriInPlayer(step);
        }
      }
    });
  }

  private void initializePlayer() {
    if (exoPlayer == null) {
      exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(),
          new DefaultLoadControl());
      playerView.setPlayer(exoPlayer);
    }
  }

  private void setUriInPlayer(Step step) {
    Context context = getContext();

    String userAgent = Util.getUserAgent(context, "BakingTime");

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
}
