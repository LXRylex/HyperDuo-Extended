# HyperDuo Heart

A modified fork of [HyperDuo](https://github.com/yixing233/HyperDuo) with a heart-shaped battery indicator and matching status-bar colors for compatible Xiaomi HyperOS devices. The original project is credited in [README.upstream.md](README.upstream.md). Both are licensed under [GPL-3.0](LICENSE).

## Download and use

Install the latest [heart.8 APK](https://github.com/LXRylex/HyperDuo-Extended/releases/tag/v1.7.4-heart.8); its [SHA-256 checksum](release/SHA256SUMS.txt) is included. Enable the module in a compatible modern LSPosed/Vector loader and scope it to `com.android.systemui`. Open HyperDuo, enable it, and select **Heart** under Drawing. Restart SystemUI using your module manager if needed.

The heart fills from the bottom according to battery level. Settings include normal, charging, low and full colors; a charging wave; percentage inside or beside the heart; automatic outline tint; and matching status-bar and HyperOS shade header colors. Default, Ring and Capsule styles are retained.

Tested on a Redmi Note 13 Pro 4G (emerald), HyperOS `OS3.0.305.0.WNFMIXM`, Android 16, with a libxposed API 102 loader. SystemUI classes vary by ROM; other devices may behave differently.
