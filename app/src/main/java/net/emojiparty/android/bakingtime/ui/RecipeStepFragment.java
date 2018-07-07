package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.emojiparty.android.bakingtime.BR;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Step;
import net.emojiparty.android.bakingtime.data.StepPresenter;

public class RecipeStepFragment extends Fragment {
  private RecipeDetailViewModel detailViewModel;
  private View root;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    ViewDataBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
    root = binding.getRoot();
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
      }
    });
  }
}
