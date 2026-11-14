package org.rudy.chat.ui.Components.Premium.boosts.cells;

import static org.rudy.chat.messenger.AndroidUtilities.dp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;

import androidx.annotation.NonNull;

import org.rudy.chat.messenger.AndroidUtilities;
import org.rudy.chat.messenger.LocaleController;
import org.rudy.chat.ui.ActionBar.Theme;
import org.rudy.chat.ui.ActionBar.ThemeBind;
import org.rudy.chat.ui.Components.AnimatedTextView;
import org.rudy.chat.ui.Components.CubicBezierInterpolator;
import org.rudy.chat.ui.Components.LayoutHelper;

@SuppressLint("ViewConstructor")
public class SubtitleWithCounterCell extends org.rudy.chat.ui.Cells.HeaderCell {

    private final AnimatedTextView counterTextView;

    public SubtitleWithCounterCell(@NonNull Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);

        counterTextView = new AnimatedTextView(context, true, true, true);
        counterTextView.setAnimationProperties(.45f, 0, 240, CubicBezierInterpolator.EASE_OUT_QUINT);
        counterTextView.setGravity(LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT);
        counterTextView.setTextSize(dp(15));
        counterTextView.setTypeface(AndroidUtilities.bold());
        ThemeBind.textColor(counterTextView, Theme.key_windowBackgroundWhiteBlueHeader, resourcesProvider);
        addView(counterTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 24, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.BOTTOM, 24, 0, 24, 0));
        ThemeBind.backgroundColor(this, Theme.key_dialogBackground, resourcesProvider);
    }

    public void updateCounter(boolean animated, int count) {
        CharSequence text = count <= 0 ? "" : LocaleController.formatPluralString("BoostingBoostsCountTitle", count, count);
        counterTextView.cancelAnimation();
        counterTextView.setText(text, animated);
    }
}
