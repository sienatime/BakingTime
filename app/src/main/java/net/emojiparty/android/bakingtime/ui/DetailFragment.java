package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.emojiparty.android.bakingtime.BR;
import net.emojiparty.android.bakingtime.R;

public class DetailFragment extends Fragment {
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    ViewDataBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

    RecipeDetailViewModel detailViewModel =
        ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
    binding.setLifecycleOwner(getActivity());
    binding.setVariable(BR.presenter, detailViewModel);

    return binding.getRoot();
  }
}
