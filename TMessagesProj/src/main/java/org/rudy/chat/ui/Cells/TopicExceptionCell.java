package org.rudy.chat.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.rudy.chat.messenger.AndroidUtilities;
import org.rudy.chat.messenger.MessagesController;
import org.rudy.chat.messenger.UserConfig;
import org.rudy.chat.messenger.FileLog;
import org.rudy.chat.tgnet.TLRPC;
import org.rudy.chat.ui.ActionBar.Theme;
import org.rudy.chat.ui.ActionBar.ThemeBind;
import org.rudy.chat.ui.Components.BackupImageView;
import org.rudy.chat.ui.Components.Forum.ForumUtilities;
import org.rudy.chat.ui.Components.LayoutHelper;

public class TopicExceptionCell extends FrameLayout implements Theme.Colorable {

    public boolean drawDivider;
    BackupImageView backupImageView;
    TextView title;
    TextView subtitle;

    public TopicExceptionCell(Context context) {
        super(context);
        backupImageView = new BackupImageView(context);
        addView(backupImageView, LayoutHelper.createFrame(30, 30, Gravity.CENTER_VERTICAL, 20, 0, 0, 0));

        title = new TextView(context);
        ThemeBind.textColor(title, Theme.key_windowBackgroundWhiteBlackText);
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        title.setTypeface(AndroidUtilities.bold());
        title.setMaxLines(1);
        addView(title, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 72, 8, 12, 0));


        subtitle = new TextView(context);
        ThemeBind.textColor(subtitle, Theme.key_windowBackgroundWhiteGrayText);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        addView(subtitle, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 72, 32, 12, 0));

    }

    public void setTopic(long dialogId, TLRPC.TL_forumTopic topic) {
        ForumUtilities.setTopicIcon(backupImageView, topic);
        if (backupImageView != null && backupImageView.getImageReceiver() != null && backupImageView.getImageReceiver().getDrawable() instanceof ForumUtilities.GeneralTopicDrawable) {
            ((ForumUtilities.GeneralTopicDrawable) backupImageView.getImageReceiver().getDrawable()).setColor(Theme.getColor(Theme.key_chats_archiveBackground));
        }
        title.setText(topic.title);
        subtitle.setText(MessagesController.getInstance(UserConfig.selectedAccount).getMutedString(dialogId, topic.id));
    }

    @Override
    public void updateColors() {
        try {
            if (backupImageView != null && backupImageView.getImageReceiver() != null && backupImageView.getImageReceiver().getDrawable() instanceof ForumUtilities.GeneralTopicDrawable) {
                ((ForumUtilities.GeneralTopicDrawable) backupImageView.getImageReceiver().getDrawable()).setColor(Theme.getColor(Theme.key_chats_archiveBackground));
                backupImageView.invalidate();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override
    public void addColorKeys(java.util.HashSet<Integer> colors) {
        colors.add(Theme.key_chats_archiveBackground);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56), MeasureSpec.EXACTLY));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawDivider) {
            canvas.drawLine(AndroidUtilities.dp(72), getMeasuredHeight() - 1, getMeasuredWidth(), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }
}
