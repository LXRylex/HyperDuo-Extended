// SPDX-License-Identifier: GPL-3.0-only
package io.github.yixing233.hyperduo;
/** Geometry shared by the native measurement hook and the Heart renderer. */
final class HeartBatteryLayout {
    private HeartBatteryLayout() {}
    static boolean outside(TrioSettings cfg) {
        return cfg!=null && cfg.trioStyle==Prefs.STYLE_HEART && cfg.heartPercentage && cfg.heartPercentageRight;
    }
    static float designWidth(TrioSettings cfg) { return outside(cfg) ? 210f : 100f; }
    static int measuredWidth(int nativeWidth,int height,TrioSettings cfg) {
        if (!outside(cfg) || height<=0) return nativeWidth;
        // Reserve three digits without resizing the heart or reflowing at 9/10/100%.
        return Math.max(nativeWidth,(int)Math.ceil(height*designWidth(cfg)/100f));
    }
}
