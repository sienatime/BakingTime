package net.emojiparty.android.bakingtime.ui.recipe_detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import net.emojiparty.android.bakingtime.BR;
import net.emojiparty.android.bakingtime.R;
import net.emojiparty.android.bakingtime.data.models.Recipe;
import net.emojiparty.android.bakingtime.data.models.Step;
import net.emojiparty.android.bakingtime.ui.DataBindingAdapter;

public class RecipeFragment extends Fragment {
  private RecipeDetailViewModel detailViewModel;
  private View root;
  private RecipeDetailActivity.OnStepClicked onStepClicked;

  public void setOnStepClicked(RecipeDetailActivity.OnStepClicked onStepClicked) {
    this.onStepClicked = onStepClicked;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    ViewDataBinding binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_recipe, container, false);
    root = binding.getRoot();
    setupViewModel(binding);
    setupStepsRecyclerView();
    return root;
  }

  private void setupViewModel(ViewDataBinding binding) {
    FragmentActivity activity = getActivity();
    detailViewModel = ViewModelProviders.of(activity).get(RecipeDetailViewModel.class);
    binding.setLifecycleOwner(activity);
    binding.setVariable(BR.presenter, detailViewModel);
  }

  private void setupStepsRecyclerView() {
    RecyclerView stepsRecyclerView = root.findViewById(R.id.steps_recycler_view);
    stepsRecyclerView.setNestedScrollingEnabled(false);
    final DataBindingAdapter adapter = new DataBindingAdapter(R.layout.list_item_step);
    stepsRecyclerView.setAdapter(adapter);

    detailViewModel.getRecipe().observe(this, new Observer<Recipe>() {
      @Override public void onChanged(@Nullable Recipe recipe) {
        adapter.setItems(mapStepsToPresenters(recipe.getSteps()));
      }
    });
  }

  private List<StepInListPresenter> mapStepsToPresenters(List<Step> steps) {
    List<StepInListPresenter> presenters = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      Step step = steps.get(i);
      presenters.add(new StepInListPresenter(step, detailViewModel, onStepClicked));
    }
    return presenters;
  }
}
