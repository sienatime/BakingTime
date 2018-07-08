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

  private void setupViewModel(final ViewDataBinding binding) {
    FragmentActivity activity = getActivity();
    detailViewModel = ViewModelProviders.of(activity).get(RecipeDetailViewModel.class);
    binding.setLifecycleOwner(activity);
    detailViewModel.getSelectedStep().observe(activity, new Observer<Step>() {
      @Override public void onChanged(@Nullable Step step) {
        StepPresenter stepPresenter = new StepPresenter(detailViewModel);
        binding.setVariable(BR.presenter, stepPresenter);
        initializePlayer(Uri.parse(step.getVideoURL()));
      }
    });
  }

  private void initializePlayer(Uri videoUri) {
    Context context = getContext();
    String userAgent = Util.getUserAgent(context, "BakingTime");

    // only need to do this once per fragment
    if (exoPlayer == null) {
      exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(),
          new DefaultLoadControl());
      playerView.setPlayer(exoPlayer);
    }

    // but this needs to be refreshed with the new videoUri when the step has changed
    MediaSource mediaSource =
        new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(context, userAgent),
            new DefaultExtractorsFactory(), null, null);
    exoPlayer.prepare(mediaSource);
    exoPlayer.setPlayWhenReady(true);
  }

  private void releasePlayer() {
    exoPlayer.stop();
    exoPlayer.release();
    exoPlayer = null;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    releasePlayer();
  }
}
