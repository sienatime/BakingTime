package net.emojiparty.android.bakingtime.data;

import android.support.annotation.Nullable;
import net.emojiparty.android.bakingtime.ui.RecipeDetailActivity;
import net.emojiparty.android.bakingtime.ui.RecipeDetailViewModel;

public class StepPresenter {
  private Step step;
  private RecipeDetailViewModel viewModel;
  @Nullable private RecipeDetailActivity.OnStepClicked onStepClicked;

  public StepPresenter(Step step, RecipeDetailViewModel viewModel,
      RecipeDetailActivity.OnStepClicked onStepClicked) {
    this.step = step;
    this.viewModel = viewModel;
    this.onStepClicked = onStepClicked;
  }

  public Step getStep() {
    return step;
  }

  public void onStepClicked() {
    viewModel.setSelectedStep(step);
    if (onStepClicked != null) {
      onStepClicked.onClick();
    }
  }
}
