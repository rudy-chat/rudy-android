package org.rudy.chat.ui.ActionBar;

/**
 * Rudy's themes. Three base themes are recolored with Telegram's standard accent system, which also picks readable
 * (black or white) text for accent-colored bubbles and buttons:
 *   Light ("Blue" internally, rudy_day.attheme), Dark ("Dark Blue", rudy_night.attheme), Black ("Night", rudy_black.attheme).
 *
 * The accent replaces the base theme's blue everywhere: buttons, links, switches, badges, the compose button, your
 * own message bubbles and the Rudy wordmark. Index 0 is each theme's default accent.
 *
 * Colors are kept at medium saturation so they are easy on the eyes: light accents read on white as link text
 * (>= 4.5:1) and carry white text; dark accents read on near-black, and dark bubbles carry white text.
 */
public final class RudyTheme {

    private RudyTheme() {
    }

    /** The blue the bundled base themes were designed around; accents are applied relative to it. */
    public static final int DAY_ASSET_ACCENT = 0xFF4C91DF;
    public static final int NIGHT_ASSET_ACCENT = 0xFF3E88F7;

    public static final int INK = 0xFF111113;

    // Saved ids: never renumber or reuse. Telegram lists the home accent (index 0) first, then the others by
    // descending id, so the array order below is the order shown in Settings.
    public static final int[] ACCENT_IDS = {0, 18, 17, 16, 15, 14, 13, 12, 11};

    //                                          Black       Pink        Purple      Red         Ocean       Mint        Amber       Graphite    Maroon
    public static final int[] LIGHT_ACCENTS = {INK,        0xFFBF4277, 0xFF7450C4, 0xFFC8453F, 0xFF2D6FB8, 0xFF187A5E, 0xFF9C6A17, 0xFF55555C, 0xFF800000};

    //                                          White       Pink        Purple      Red         Ocean       Mint        Amber       Graphite    Maroon
    public static final int[] DARK_ACCENTS  = {0xFFEDEDF0, 0xFFEC7FA9, 0xFFAC93EC, 0xFFEE8079, 0xFF73A9EA, 0xFF55C7A3, 0xFFE2B35F, 0xFFAEAEB5, 0xFFD27373};
    /** Your own message bubbles on Dark and Black: graphite for White, deep calm tones for the colors. */
    public static final int[] DARK_BUBBLES  = {0xFF3A3A3F, 0xFF8E3560, 0xFF56419A, 0xFF953834, 0xFF285A93, 0xFF1A6A53, 0xFF7E5B17, 0xFF46464C, 0xFF6E1A1A};

    // default chat background of the light theme (rudy.chat: #e9e9e5 -> #d9d9d4)
    public static final int[] DAY_WALLPAPER = {0xffe9e9e5, 0xffdcdcd7, 0xffe4e4e0, 0xffd9d9d4};

    // avatars without a photo on the default (black / white) accent: no per-person colors, just ink or graphite
    private static final int[] NEUTRAL_AVATAR_LIGHT = {0xFF2C2C30, INK};
    private static final int[] NEUTRAL_AVATAR_DARK = {0xFF5A5A61, 0xFF46464C};

    /**
     * The top and bottom colors for avatars without a photo when the app runs one of Rudy's own themes on its default
     * accent (black in Light, white in Dark and Black); null otherwise, so other accents and cloud themes keep
     * Telegram's per-person colors.
     */
    public static int[] neutralAvatarColors() {
        final Theme.ThemeInfo theme = Theme.getActiveTheme();
        if (theme == null || theme.assetName == null || !theme.assetName.startsWith("rudy_") || theme.currentAccentId != 0) {
            return null;
        }
        return theme.isDark() ? NEUTRAL_AVATAR_DARK : NEUTRAL_AVATAR_LIGHT;
    }
}
