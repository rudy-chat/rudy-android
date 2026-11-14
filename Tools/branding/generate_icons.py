#!/usr/bin/env python3
"""Regenerates every Rudy logo asset (launcher icons, notification icon, in-app logos) from one source image.

Usage (from the repository root):
    python3 Tools/branding/generate_icons.py [branding/rudy_logo_source.png]

The source should be dark line art on a light background. Existing assets are overwritten in place, keeping
their file names, sizes and formats so no layout or code has to change.
"""
import glob
import os
import sys

from PIL import Image, ImageChops, ImageDraw, ImageFilter, ImageOps

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
SOURCE = sys.argv[1] if len(sys.argv) > 1 else os.path.join(ROOT, "branding", "rudy_logo_source.png")

INK = (0, 0, 0, 255)          # logo color on light surfaces
BACKGROUND = (255, 255, 255, 255)
BRAND_BACKGROUND_HEX = "#FFFFFF"

DENSITIES = {"mdpi": 1.0, "hdpi": 1.5, "xhdpi": 2.0, "xxhdpi": 3.0, "xxxhdpi": 4.0}


def load_mask(path):
    """Alpha mask of the logo's ink, cropped to a square around its content."""
    gray = Image.open(path).convert("L")
    mask = ImageOps.invert(gray)
    mask = mask.point(lambda v: 0 if v < 40 else min(255, int((v - 40) * 255 / 175)))
    left, top, right, bottom = mask.getbbox()
    side = max(right - left, bottom - top)
    cx, cy = (left + right) / 2, (top + bottom) / 2
    box = (int(cx - side / 2), int(cy - side / 2), int(cx + side / 2), int(cy + side / 2))
    square = Image.new("L", (side, side), 0)
    square.paste(mask.crop(box), (0, 0))
    return square


MASK = load_mask(SOURCE)


def logo_mask(size, scale, bold=0):
    """The logo mask scaled to `scale` of a `size` canvas (width, height), centered."""
    w, h = size
    target = max(1, int(min(w, h) * scale))
    m = MASK
    if bold:
        # thicken strokes before downscaling so tiny icons stay legible
        m = m.filter(ImageFilter.MaxFilter(bold))
    m = m.resize((target, target), Image.LANCZOS)
    canvas = Image.new("L", (w, h), 0)
    canvas.paste(m, ((w - target) // 2, (h - target) // 2))
    return canvas


def colored(mask, color):
    img = Image.new("RGBA", mask.size, color[:3] + (0,))
    img.putalpha(ImageChops.multiply(mask, Image.new("L", mask.size, color[3])))
    return img


def legacy_icon(px, round_shape):
    """Pre-Oreo launcher icon: logo on a white circle / rounded square."""
    ss = 4
    big = px * ss
    shape = Image.new("L", (big, big), 0)
    d = ImageDraw.Draw(shape)
    inset = int(big * 0.04)
    if round_shape:
        d.ellipse((inset, inset, big - inset, big - inset), fill=255)
    else:
        d.rounded_rectangle((inset, inset, big - inset, big - inset), radius=int(big * 0.2), fill=255)
    shape = shape.resize((px, px), Image.LANCZOS)
    img = Image.new("RGBA", (px, px), BACKGROUND[:3] + (0,))
    img.putalpha(shape)
    logo = colored(logo_mask((px, px), 0.62, bold=5 if px <= 72 else 0), INK)
    return Image.alpha_composite(img, logo)


def save(img, path):
    ext = os.path.splitext(path)[1].lower()
    if ext == ".webp":
        img.save(path, "WEBP", lossless=True)
    else:
        img.save(path, "PNG", optimize=True)
    print("wrote", os.path.relpath(path, ROOT))


def each(pattern):
    return sorted(glob.glob(os.path.join(ROOT, pattern)))


def main():
    res_dirs = [os.path.join(ROOT, p) for p in ("TMessagesProj/src/main/res", "TMessagesProj_AppStandalone/src/main/res")]

    # Adaptive icon layers (108dp canvas, logo kept inside the 66dp safe zone).
    main_res = res_dirs[0]
    for density, factor in DENSITIES.items():
        px = int(108 * factor)
        d = os.path.join(main_res, "mipmap-" + density)
        os.makedirs(d, exist_ok=True)
        save(colored(logo_mask((px, px), 0.50), INK), os.path.join(d, "rudy_icon_foreground.png"))
        save(colored(logo_mask((px, px), 0.50, bold=3), (255, 255, 255, 255)), os.path.join(d, "rudy_icon_monochrome.png"))

    adaptive = (
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
        '    <background android:drawable="@color/rudy_icon_background" />\n'
        '    <foreground android:drawable="@mipmap/rudy_icon_foreground" />\n'
        '    <monochrome android:drawable="@mipmap/rudy_icon_monochrome" />\n'
        '</adaptive-icon>\n'
    )
    for res in res_dirs:
        for xml in each(os.path.relpath(res, ROOT) + "/mipmap-anydpi-v26/*.xml"):
            with open(xml, "w") as f:
                f.write(adaptive)
            print("wrote", os.path.relpath(xml, ROOT))

    values = os.path.join(main_res, "values", "rudy_branding.xml")
    with open(values, "w") as f:
        f.write('<?xml version="1.0" encoding="utf-8"?>\n<resources>\n'
                '    <color name="rudy_icon_background">%s</color>\n</resources>\n' % BRAND_BACKGROUND_HEX)
    print("wrote", os.path.relpath(values, ROOT))

    for res in res_dirs:
        rel = os.path.relpath(res, ROOT)
        # Legacy launcher icons, including the alternate-icon variants, all become the Rudy logo.
        for path in each(rel + "/mipmap-*/*launcher*.png"):
            px = Image.open(path).size[0]
            save(legacy_icon(px, "round" in os.path.basename(path)), path)

    # Contact/account/voip avatar icon.
    for path in each("TMessagesProj/src/main/res/drawable-*/ic_launcher_dr.webp"):
        save(legacy_icon(Image.open(path).size[0], True), path)

    # Logo on the Terms of Service screen.
    for path in each("TMessagesProj/src/main/res/drawable-*/logo_middle.*"):
        save(legacy_icon(Image.open(path).size[0], True), path)

    # Status bar notification icon: white silhouette.
    for path in each("TMessagesProj/src/main/res/drawable-*/notification.webp"):
        px = Image.open(path).size[0]
        save(colored(logo_mask((px, px), 0.92, bold=9 if px <= 48 else 7), (255, 255, 255, 255)), path)

    # White logo drawn on the intro screen's sphere.
    for path in each("TMessagesProj/src/main/res/drawable-*/intro_tg_plane.webp"):
        size = Image.open(path).size
        save(colored(logo_mask(size, 0.95, bold=3), (255, 255, 255, 255)), path)

    # README / store artwork.
    out = os.path.join(ROOT, "branding")
    os.makedirs(out, exist_ok=True)
    save(legacy_icon(512, False), os.path.join(out, "rudy_icon_512.png"))
    save(colored(logo_mask((512, 512), 0.9), INK), os.path.join(out, "rudy_logo.png"))


if __name__ == "__main__":
    main()
