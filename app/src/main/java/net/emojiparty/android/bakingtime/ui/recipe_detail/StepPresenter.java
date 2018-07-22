package net.emojiparty.android.bakingtime.ui.recipe_detail;

public class StepPresenter {
  private RecipeDetailViewModel viewModel;
  private OnStepChanged onStepChanged;

  public StepPresenter(RecipeDetailViewModel viewModel, OnStepChanged onStepChanged) {
    this.viewModel = viewModel;
    this.onStepChanged = onStepChanged;
  }

  public RecipeDetailViewModel getViewModel() {
    return viewModel;
  }

  public void onNextStepClicked() {
    onStepChanged.callback();
    viewModel.setSelectedStep(viewModel.getNextStep().getValue());
  }

  public void onPreviousStepClicked() {
    onStepChanged.callback();
    viewModel.setSelectedStep(viewModel.getPreviousStep().getValue());
  }

  public interface OnStepChanged {
    void callback();
  }
}
