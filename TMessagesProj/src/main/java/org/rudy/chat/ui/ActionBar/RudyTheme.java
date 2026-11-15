package org.rudy.chat.ui.ActionBar;

/**
 * Rudy's themes. Two base themes (light: rudy_day.attheme, dark: rudy_night.attheme) are recolored with Telegram's
 * standard accent system, which also picks readable (black or white) text for accent-colored bubbles and buttons.
 *
 * The accent replaces the base theme's blue everywhere: buttons, links, switches, badges, the compose button, your
 * own message bubbles and the Rudy wordmark. Index 0 is the default accent of each theme.
 */
public final class RudyTheme {

    private RudyTheme() {
    }

    /** The blue the bundled base themes were designed around; accents are applied relative to it. */
    public static final int DAY_ASSET_ACCENT = 0xFF4C91DF;
    public static final int NIGHT_ASSET_ACCENT = 0xFF3E88F7;

    public static final int INK = 0xFF111113;

    // Saved ids: never renumber. Telegram lists the home accent (index 0) first, then the others by descending id.
    public static final int[] ACCENT_IDS = {0, 6, 5, 4, 3, 2, 1};

    //                                          Black       Pink        Lavender    Mint        Coral       Ocean       Gold
    public static final int[] LIGHT_ACCENTS = {INK,        0xFFE0508A, 0xFF8466D6, 0xFF239A78, 0xFFE2603F, 0xFF2D7DCF, 0xFFA87612};

    //                                          White       Pink        Lavender    Mint        Coral       Ocean       Gold
    public static final int[] DARK_ACCENTS  = {0xFFF1F1F3, 0xFFF06A9E, 0xFFA58BF0, 0xFF3CC39B, 0xFFF7826A, 0xFF4A9BEA, 0xFFE0AE45};
    /** Your own message bubbles in the dark theme: graphite for White, deeper tones for the colors. */
    public static final int[] DARK_BUBBLES  = {0xFF3A3A3F, 0xFFB03A6C, 0xFF6A52B8, 0xFF1F7D62, 0xFFB44A32, 0xFF2766A8, 0xFF94701C};

    // default chat background of the light theme (rudy.chat: #e9e9e5 -> #d9d9d4)
    public static final int[] DAY_WALLPAPER = {0xffe9e9e5, 0xffdcdcd7, 0xffe4e4e0, 0xffd9d9d4};
}
