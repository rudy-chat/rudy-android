package org.rudy.chat.ui.ActionBar;

import android.graphics.Color;
import android.util.SparseIntArray;

/**
 * Rudy's black-and-white look, taken from rudy.chat.
 *
 * Every color table the app resolves (the built-in defaults, the bundled themes and the accent-tinted tables) passes
 * through {@link #apply}. Colors are made monochrome (red stays, so destructive actions remain recognizable), accent
 * colors become ink, and the main surfaces get the exact palette values. Gradient wallpapers go through {@link #mono}.
 */
public final class RudyTheme {

    private RudyTheme() {
    }

    // rudy.chat palette
    public static final int INK = 0xff111113;
    public static final int PAPER = 0xffffffff;
    public static final int SMOKE = 0xfff0f0ed;
    public static final int GRAPHITE = 0xff6a6a6f;
    public static final int LINE = 0xffe1e1dd;
    public static final int HINT = 0xff8e8e93;

    public static final int NIGHT_PAPER = 0xff121214;
    public static final int NIGHT_BAR = 0xff1b1b1e;
    public static final int NIGHT_INK = 0xfff1f1f3;
    public static final int NIGHT_GRAPHITE = 0xff9a9aa0;
    public static final int NIGHT_LINE = 0xff2a2a2e;

    // chat wallpaper gradients (day: #e9e9e5 → #d9d9d4, night: #1c1c1f → #141416)
    public static final int[] DAY_WALLPAPER = {0xffe9e9e5, 0xffdcdcd7, 0xffe4e4e0, 0xffd9d9d4};
    public static final int[] NIGHT_WALLPAPER = {0xff1c1c1f, 0xff151517, 0xff19191b, 0xff141416};

    private static final float[] hsv = new float[3];

    private static boolean isRed(int color) {
        synchronized (hsv) {
            Color.colorToHSV(color, hsv);
            return hsv[1] > 0.35f && hsv[2] > 0.3f && (hsv[0] < 18f || hsv[0] > 335f);
        }
    }

    private static float saturation(int color) {
        synchronized (hsv) {
            Color.colorToHSV(color, hsv);
            return hsv[1] * hsv[2];
        }
    }

    private static int gray(int alpha, int v) {
        v = Math.max(0, Math.min(255, v));
        // a hair of blue in the darks, like #111113
        int b = Math.min(255, v + (v < 40 ? 2 : 0));
        return (alpha << 24) | (v << 16) | (v << 8) | b;
    }

    private static int luminance(int color) {
        return (int) (0.299f * Color.red(color) + 0.587f * Color.green(color) + 0.114f * Color.blue(color));
    }

    /** The color without hue, same brightness. Red is kept. */
    public static int mono(int color) {
        if (color == 0 || isRed(color)) {
            return color;
        }
        return gray(Color.alpha(color), luminance(color));
    }

    /** Accent colors (strong hue) become ink on light surfaces and near-white on dark ones. */
    private static int monoAccent(int color, boolean dark) {
        if (color == 0 || isRed(color)) {
            return color;
        }
        int a = Color.alpha(color);
        int l = luminance(color);
        if (saturation(color) < 0.22f) {
            return gray(a, l);
        }
        if (dark) {
            return gray(a, 205 + l / 6);
        }
        return l > 200 ? gray(a, l) : gray(a, 17 + l / 8);
    }

    /** Inverts brightness, for message bubbles that turn from light to ink. */
    private static int invert(int color) {
        if (color == 0 || isRed(color)) {
            return color;
        }
        return gray(Color.alpha(color), 255 - luminance(color));
    }

    public static void apply(int[] colors, boolean dark) {
        SparseIntArray table = new SparseIntArray(colors.length);
        for (int key = 0; key < colors.length; key++) {
            table.put(key, colors[key]);
        }
        apply(table, dark);
        for (int key = 0; key < colors.length; key++) {
            colors[key] = table.get(key, colors[key]);
        }
    }

    public static void apply(SparseIntArray colors, boolean dark) {
        for (int i = 0, n = colors.size(); i < n; i++) {
            final int key = colors.keyAt(i);
            final int color = colors.valueAt(i);
            final String name = ThemeColors.getStringName(key);
            int result;
            if (name == null) {
                result = mono(color);
            } else if (name.startsWith("avatar_background") || name.startsWith("avatar_backgroundGradient")) {
                // avatars: soft gray gradients, like the site's chat list
                result = gray(Color.alpha(color), dark ? 70 + luminance(color) / 4 : 90 + luminance(color) / 3);
            } else if (name.startsWith("avatar_nameInMessage")) {
                result = gray(255, dark ? 215 : 60);
            } else if (!dark && (name.startsWith("chat_out") || name.equals("chat_messageTextOut") || name.equals("chat_messageLinkOut")
                    || name.equals("chat_outBubble") || name.startsWith("chat_messageOut"))) {
                // outgoing bubbles are ink with light text in the day theme
                result = invert(monoAccent(color, false));
            } else {
                result = monoAccent(color, dark);
            }
            colors.setValueAt(i, result);
        }
        if (dark) {
            applyNight(colors);
        } else {
            applyDay(colors);
        }
    }

    private static void put(SparseIntArray colors, int key, int color) {
        colors.put(key, color);
    }

    private static void applyDay(SparseIntArray c) {
        // bars
        put(c, Theme.key_actionBarDefault, PAPER);
        put(c, Theme.key_actionBarDefaultTitle, INK);
        put(c, Theme.key_actionBarDefaultIcon, INK);
        put(c, Theme.key_actionBarDefaultSubtitle, HINT);
        put(c, Theme.key_actionBarDefaultSelector, 0x14000000);
        put(c, Theme.key_actionBarDefaultSearch, INK);
        put(c, Theme.key_actionBarDefaultSearchPlaceholder, 0x88111113);
        put(c, Theme.key_actionBarDefaultSubmenuBackground, PAPER);
        put(c, Theme.key_actionBarDefaultSubmenuItem, INK);
        put(c, Theme.key_actionBarDefaultSubmenuItemIcon, GRAPHITE);
        put(c, Theme.key_actionBarTabActiveText, INK);
        put(c, Theme.key_actionBarTabUnactiveText, HINT);
        put(c, Theme.key_actionBarTabLine, INK);
        put(c, Theme.key_actionBarTabSelector, 0x14000000);
        put(c, Theme.key_actionBarDefaultArchived, PAPER);
        put(c, Theme.key_actionBarDefaultArchivedTitle, INK);
        put(c, Theme.key_actionBarDefaultArchivedIcon, INK);
        put(c, Theme.key_actionBarDefaultArchivedSelector, 0x14000000);
        put(c, Theme.key_actionBarDefaultArchivedSearch, INK);
        put(c, Theme.key_actionBarDefaultArchivedSearchPlaceholder, 0x88111113);

        // surfaces and text
        put(c, Theme.key_windowBackgroundWhite, PAPER);
        put(c, Theme.key_windowBackgroundGray, SMOKE);
        put(c, Theme.key_windowBackgroundWhiteBlackText, INK);
        put(c, Theme.key_windowBackgroundWhiteGrayText, GRAPHITE);
        put(c, Theme.key_windowBackgroundWhiteGrayText2, GRAPHITE);
        put(c, Theme.key_windowBackgroundWhiteBlueText, INK);
        put(c, Theme.key_windowBackgroundWhiteBlueText4, INK);
        put(c, Theme.key_windowBackgroundWhiteBlueHeader, INK);
        put(c, Theme.key_windowBackgroundWhiteLinkText, INK);
        put(c, Theme.key_divider, LINE);
        put(c, Theme.key_dialogBackground, PAPER);
        put(c, Theme.key_dialogTextBlack, INK);
        put(c, Theme.key_dialogTextBlue, INK);
        put(c, Theme.key_dialogButton, INK);

        // chat list
        put(c, Theme.key_chats_name, 0xff151515);
        put(c, Theme.key_chats_message, 0xff717172);
        put(c, Theme.key_chats_date, 0xff818181);
        put(c, Theme.key_chats_unreadCounter, 0xff202023);
        put(c, Theme.key_chats_unreadCounterMuted, 0xffb7b7b8);
        put(c, Theme.key_chats_unreadCounterText, PAPER);
        put(c, Theme.key_chats_actionBackground, INK);
        put(c, Theme.key_chats_actionPressedBackground, 0xff2b2b2f);
        put(c, Theme.key_chats_actionIcon, PAPER);
        put(c, Theme.key_chats_sentCheck, 0xff1f1f21);
        put(c, Theme.key_chats_sentReadCheck, 0xff1f1f21);
        put(c, Theme.key_chats_pinnedIcon, 0xff8f8f8f);
        put(c, Theme.key_chats_menuBackground, PAPER);
        put(c, Theme.key_chats_menuTopBackgroundCats, INK);
        put(c, Theme.key_chats_menuName, PAPER);
        put(c, Theme.key_chats_menuPhone, 0xffb4b4b8);

        // chat
        put(c, Theme.key_chat_inBubble, PAPER);
        put(c, Theme.key_chat_messageTextIn, 0xff000000);
        put(c, Theme.key_chat_inTimeText, 0xff939395);
        put(c, Theme.key_chat_outBubble, INK);
        put(c, Theme.key_chat_outBubbleSelected, 0xff2b2b2f);
        put(c, Theme.key_chat_messageTextOut, PAPER);
        put(c, Theme.key_chat_messageLinkOut, 0xffe6e6ea);
        put(c, Theme.key_chat_outTimeText, 0xffa5a5ab);
        put(c, Theme.key_chat_outSentCheck, 0xffa5a5ab);
        put(c, Theme.key_chat_outSentCheckRead, PAPER);
        put(c, Theme.key_chat_outSentClock, 0xffa5a5ab);
        put(c, Theme.key_chat_outViews, 0xffa5a5ab);
        put(c, Theme.key_chat_wallpaper, DAY_WALLPAPER[0]);
        put(c, Theme.key_chat_wallpaper_gradient_to1, DAY_WALLPAPER[1]);
        put(c, Theme.key_chat_wallpaper_gradient_to2, DAY_WALLPAPER[2]);
        put(c, Theme.key_chat_wallpaper_gradient_to3, DAY_WALLPAPER[3]);
        put(c, Theme.key_chat_messagePanelBackground, PAPER);
        put(c, Theme.key_chat_messagePanelHint, 0xffa0a0a3);
        put(c, Theme.key_chat_messagePanelIcons, 0xff848485);
        put(c, Theme.key_chat_messagePanelSend, INK);

        // controls
        put(c, Theme.key_switchTrackChecked, INK);
        put(c, Theme.key_checkboxSquareBackground, INK);
        put(c, Theme.key_radioBackgroundChecked, INK);
        put(c, Theme.key_featuredStickers_addButton, INK);
        put(c, Theme.key_featuredStickers_addButtonPressed, 0xff2b2b2f);
        put(c, Theme.key_featuredStickers_buttonText, PAPER);
    }

    private static void applyNight(SparseIntArray c) {
        put(c, Theme.key_actionBarDefault, NIGHT_BAR);
        put(c, Theme.key_actionBarDefaultTitle, 0xffffffff);
        put(c, Theme.key_actionBarDefaultIcon, 0xffffffff);
        put(c, Theme.key_actionBarDefaultSubtitle, 0xff8f8f95);
        put(c, Theme.key_actionBarDefaultSubmenuBackground, 0xff222225);
        put(c, Theme.key_actionBarTabActiveText, NIGHT_INK);
        put(c, Theme.key_actionBarTabLine, NIGHT_INK);
        put(c, Theme.key_actionBarDefaultArchived, NIGHT_BAR);

        put(c, Theme.key_windowBackgroundWhite, NIGHT_PAPER);
        put(c, Theme.key_windowBackgroundGray, 0xff0c0c0e);
        put(c, Theme.key_windowBackgroundWhiteBlackText, NIGHT_INK);
        put(c, Theme.key_windowBackgroundWhiteGrayText, NIGHT_GRAPHITE);
        put(c, Theme.key_windowBackgroundWhiteGrayText2, NIGHT_GRAPHITE);
        put(c, Theme.key_windowBackgroundWhiteBlueText, NIGHT_INK);
        put(c, Theme.key_windowBackgroundWhiteBlueText4, NIGHT_INK);
        put(c, Theme.key_windowBackgroundWhiteBlueHeader, NIGHT_INK);
        put(c, Theme.key_divider, NIGHT_LINE);
        put(c, Theme.key_dialogBackground, 0xff1b1b1e);

        put(c, Theme.key_chats_unreadCounter, 0xffe6e6ea);
        put(c, Theme.key_chats_unreadCounterText, NIGHT_PAPER);
        put(c, Theme.key_chats_unreadCounterMuted, 0xff4a4a4f);
        put(c, Theme.key_chats_actionBackground, NIGHT_INK);
        put(c, Theme.key_chats_actionPressedBackground, 0xffd0d0d4);
        put(c, Theme.key_chats_actionIcon, NIGHT_PAPER);
        put(c, Theme.key_chats_menuTopBackgroundCats, 0xff1b1b1e);

        put(c, Theme.key_chat_inBubble, 0xff29292a);
        put(c, Theme.key_chat_messageTextIn, 0xfffafafa);
        put(c, Theme.key_chat_inTimeText, 0xff89898a);
        put(c, Theme.key_chat_outBubble, 0xff3a3a3f);
        put(c, Theme.key_chat_outBubbleSelected, 0xff4a4a50);
        put(c, Theme.key_chat_messageTextOut, 0xfffafafa);
        put(c, Theme.key_chat_outTimeText, 0xffa6a7a9);
        put(c, Theme.key_chat_wallpaper, NIGHT_WALLPAPER[0]);
        put(c, Theme.key_chat_wallpaper_gradient_to1, NIGHT_WALLPAPER[1]);
        put(c, Theme.key_chat_wallpaper_gradient_to2, NIGHT_WALLPAPER[2]);
        put(c, Theme.key_chat_wallpaper_gradient_to3, NIGHT_WALLPAPER[3]);
        put(c, Theme.key_chat_messagePanelBackground, 0xff282829);
        put(c, Theme.key_chat_messagePanelHint, 0xff75757a);
        put(c, Theme.key_chat_messagePanelIcons, 0xff8c8c8e);
        put(c, Theme.key_chat_messagePanelSend, NIGHT_INK);

        put(c, Theme.key_switchTrackChecked, 0xffd8d8dc);
        put(c, Theme.key_checkboxSquareBackground, 0xffd8d8dc);
        put(c, Theme.key_radioBackgroundChecked, 0xffd8d8dc);
        put(c, Theme.key_featuredStickers_addButton, NIGHT_INK);
        put(c, Theme.key_featuredStickers_addButtonPressed, 0xffd0d0d4);
        put(c, Theme.key_featuredStickers_buttonText, NIGHT_PAPER);
    }
}
