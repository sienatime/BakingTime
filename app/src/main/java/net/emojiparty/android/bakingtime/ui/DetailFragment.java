package net.emojiparty.android.bakingtime.ui;

import android.arch.lifecycle.Observer;
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
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.Recipe;
import net.emojiparty.android.bakingtime.BR;

public class DetailFragment extends Fragment {
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final ViewDataBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
    binding.setVariable(BR.presenter, new Recipe());

    RecipeDetailViewModel detailViewModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
    detailViewModel.getSelected().observe(this, new Observer<Recipe>() {
      @Override public void onChanged(@Nullable Recipe recipe) {
        binding.setVariable(BR.presenter, recipe);
        binding.executePendingBindings();
      }
    });

    return binding.getRoot();
  }
}
