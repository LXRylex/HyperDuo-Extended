// SPDX-License-Identifier: GPL-3.0-only
package io.github.yixing233.hyperduo;
/** Pure battery rules, shared by settings previews and the runtime renderer. */
public final class HeartBattery {
    private HeartBattery() {}
    /** Process-local circuit breaker. A failed Heart frame releases the native icon. */
    static volatile boolean failed;
    public static int level(int level) { return Math.max(0, Math.min(100, level)); }
    public static float fillTop(int level) { return 90f - 84f * level(level) / 100f; }
    public static int fillColor(TrioSettings s, int level, boolean charging) {
        level=level(level);
        int color=level==100 ? s.heartFull : charging ? s.heartCharging
                : level<=Math.max(1, Math.min(50, s.lowThreshold)) ? s.heartLow : s.heartNormal;
        // Transparent input would erase the only battery reading. Colors are opaque by design.
        return color | 0xff000000;
    }
}
