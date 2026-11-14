package org.rudy.chat.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import org.rudy.chat.ui.ActionBar.BaseFragment;
import org.rudy.chat.ui.Components.SizeNotifierFrameLayout;

public class EmptyBaseFragment extends BaseFragment {

    @Override
    public View createView(Context context) {
        return fragmentView = new SizeNotifierFrameLayout(context);
    }

}
