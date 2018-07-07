package net.emojiparty.android.bakingtime.data;

import android.util.Log;
import net.emojiparty.android.bakingtime.ui.RecipeDetailViewModel;

public class StepPresenter {
  private RecipeDetailViewModel viewModel;

  public StepPresenter(RecipeDetailViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public RecipeDetailViewModel getViewModel() {
    return viewModel;
  }

  public void onNextStepClicked() {
    viewModel.setSelectedStep(viewModel.getNextStep().getValue());
  }

  public void onPreviousStepClicked() {
    viewModel.setSelectedStep(viewModel.getPreviousStep().getValue());
  }
}
