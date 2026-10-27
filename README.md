# HyperDuo Heart

A modified fork of [HyperDuo](https://github.com/yixing233/HyperDuo) with a heart-shaped battery indicator and matching status-bar colors for compatible Xiaomi HyperOS devices. The original project is credited in [README.upstream.md](README.upstream.md). Both are licensed under [GPL-3.0](LICENSE).

## Download and use

Install the latest [heart.8 APK](release/HyperDuo-Heart-1.7.4-heart.8.apk); its [SHA-256 checksum](release/SHA256SUMS.txt) is included. Enable the module in a compatible modern LSPosed/Vector loader and scope it to `com.android.systemui`. Open HyperDuo, enable it, and select **Heart** under Drawing. Restart SystemUI using your module manager if needed.

The heart fills from the bottom according to battery level. Settings include normal, charging, low and full colors; a charging wave; percentage inside or beside the heart; automatic outline tint; and matching status-bar and HyperOS shade header colors. Default, Ring and Capsule styles are retained.

Tested on a Redmi Note 13 Pro 4G (emerald), HyperOS `OS3.0.305.0.WNFMIXM`, Android 16, with a libxposed API 102 loader. SystemUI classes vary by ROM; other devices may behave differently. [docs/HEART.md](docs/HEART.md) describes the modifications, testing and recovery.

## Source and build

This folder contains the complete modified source, original GPL license and vendored libxposed dependencies in `.tools/maven`. Open the folder itself in Android Studio. Requirements: JDK 21, Gradle 9.6 and Android SDK platform 37. Local SDK settings and build output are excluded.

From this directory with Gradle available:

```sh
gradle :app:assembleDebug -PhyperduoVersionCode=10712 -PhyperduoVersionName=1.7.4-heart.8
```

On Windows, run `powershell -NoProfile -ExecutionPolicy Bypass -File scripts/build-heart.ps1`. Add `-UseMavenMirror` only if Google Maven is unavailable. The output is `app/build/outputs/apk/debug/app-debug.apk`.

No signing keys are included. Debug builds use the Android build tools' local debug key. An APK signed with a different key cannot update the included APK in place; Android requires matching signatures. Configure and protect your own signing key for production releases.

Regression scripts are in `tests/`; original renderer snapshots are in `.heart-backup/` for comparison. The upstream GitHub workflow has a `.disabled` suffix because release signing needs your own configuration. Review it before enabling automation.

## Share this folder in a Git repository

This folder contains the source and APK, without Git history. Create a new repository using this folder, or copy its contents into a clone of your fork. The `.gitignore` explicitly allows the APK under `release/` to be added. To start a new local repository:

```sh
git init
git add .
git commit -m "Add HyperDuo Heart fork"
```

Then publish it with your preferred Git client. To preserve the original project's history and fork relationship, create a GitHub fork and apply these files to a clone of that fork. You can alternatively upload the APK as a GitHub Release asset.
