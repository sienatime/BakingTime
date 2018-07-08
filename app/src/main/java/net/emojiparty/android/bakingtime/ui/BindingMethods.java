package net.emojiparty.android.bakingtime.ui;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import net.emojiparty.android.bakingtime.R;

//https://android.jlelse.eu/loading-images-with-data-binding-and-picasso-555dad683fdc
public class BindingMethods {
  @BindingAdapter({"imageUrl"})
  public static void loadImageFromUrl(ImageView view, String url) {
    if (!url.equals("")) {
      Picasso.get()
          .load(url)
          .placeholder(R.color.colorPrimary)
          .error(R.color.colorAccent)
          .into(view);
    } else {
      view.setVisibility(View.INVISIBLE);
    }
  }

  @BindingAdapter({"isVisible"})
  public static void setVisibility(View view, String string) {
    if (string != null && !string.equals("")) {
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.INVISIBLE);
    }
  }

  @BindingAdapter({"isVisible"})
  public static void setVisibility(View view, Object object) {
    if (object != null) {
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.INVISIBLE);
    }
  }
}
