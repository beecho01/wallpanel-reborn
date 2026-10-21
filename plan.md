# WallPanel Reborn – Revival Plan

Working notes for reviving the archived [TheTimeWalker/wallpanel-android](https://github.com/TheTimeWalker/wallpanel-android) as **WallPanel Reborn**.
Last updated: 2026-10-21.

## Decisions (settled)

| Topic | Decision |
|---|---|
| Name | **WallPanel Reborn**. Checked on 2026-10-08: no clash on GitHub, Play Store or F-Droid. |
| Application ID / namespace | `io.github.beecho01.wallpanelreborn`. Hyphens aren't allowed in IDs, so `wallpanel-reborn` was rejected. |
| GitHub repo | `beecho01/wallpanel-reborn`. It currently exists as `Wallpanel-Reborn` and needs renaming to lowercase to match the Pages URL. |
| Website / hosting | GitHub Pages at `https://beecho01.github.io/wallpanel-reborn`, chosen because it's free. The old `wallpanel.xyz` domain is dropped. |
| Attribution | Homage: "A continuation of WallPanel by ThanksMister and TheTimeWalker." Keep the Apache 2.0 licence and the original copyright headers. |
| Icon | New adaptive (Material You) icon that keeps the original panel/window shape. |
| minSdk | 24 (Android 7.0) |
| UI | Jetpack Compose + Material 3 (phase 4) |
| WebView | The app supplies its own newer Chromium WebView for each Android tier (see below). |
| Versioning | Restart at 1.0.0 for Reborn. |
| Feedback | Opens GitHub Issues rather than email. The "Rate" entry is hidden until there's a store listing. |

Note: the upstream README offers to hand over the Play listing, signing keys and domain to a new maintainer. We chose to rebrand instead. It may still be worth contacting them later, for example to publish a final update to the old Play listing pointing users to Reborn.

## Test device

- AOSP photo-frame style device, Android **7.1.2 (API 25)**, Snapdragon 625 (`msm8953_64`), firmware build signed with release keys.
- ABIs: `arm64-v8a, armeabi-v7a, armeabi`
- System WebView: `com.android.webview` **52.0.2743.100** in `/system/app/webview`. It's the only allowed provider and can't be replaced.
- ADB over Wi-Fi: `adb connect 192.168.1.5:5555`

## Build environment

- JDK: Android Studio's JBR (JDK 21): `JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"`
- Android SDK: needs **platform android-37.0** and **build-tools 37.0.0**. Current AndroidX and Glide require compileSdk ≥ 37.
- `local.properties` needs `sdk.dir=C:/Users/<you>/AppData/Local/Android/Sdk` (use forward slashes). Optional debug defaults: `code`, `hassUrl`, `broker`, `brokerUsername`, `brokerPass`.
- Build: `./gradlew :WallPanelApp:assembleDebug`
- Install: `adb install -r WallPanelApp/build/outputs/apk/debug/WallPanelApp-arm64-v8a-debug.apk`
- Gotchas:
  - Some source files use CRLF line endings, so scripted find/replace must handle `\r\n`.
  - AGP 9 has built-in Kotlin. Don't apply `org.jetbrains.kotlin.android`, and use KSP rather than kapt.

## Phase 1: Modernise build and rebrand (IN PROGRESS, committing straight to `main`)

Done (committed in `fdc8531`):
- [x] Gradle 7.4 → **9.8.1**; AGP 7.3 → **9.4.1**; Kotlin 1.6 → **2.4.21** (built-in); kapt → **KSP 2.3.12**
- [x] Groovy → Kotlin DSL (`settings.gradle.kts`, `build.gradle.kts`, `WallPanelApp/build.gradle.kts`) plus a version catalog (`gradle/libs.versions.toml`)
- [x] minSdk 19 → 24, compileSdk 37, Java 17. **targetSdk kept at 33 for now** (see Phase 1b).
- [x] Removed Firebase (Analytics and Crashlytics), multidex, android-retrofix, retrostreams/retrofuture, legacy support libraries, Retrofit, OkHttp, RxJava, Gson, Picasso, lifecycle-extensions and LeakCanary-in-release. All were unused or unnecessary at minSdk 24.
- [x] Replaced the Crashlytics Timber trees with `utils/ReleaseTree.kt` (warnings and errors go to logcat)
- [x] Removed the product flavours (dev/qa/prod). Debug defaults now come from optional `local.properties` keys (`BuildConfig.DEV_*`).
- [x] Package renamed `xyz.wallpanel.app` → `io.github.beecho01.wallpanelreborn` (main and androidTest)
- [x] App name, notification strings and the MQTT discovery `origin` name changed to "WallPanel Reborn"
- [x] URLs changed from `wallpanel.xyz` to the GitHub Pages site; the GitHub link points to `beecho01/wallpanel-reborn`
- [x] About screen: homage credit text, feedback opens GitHub Issues, Rate button hidden
- [x] Compile fixes: AndroidAsync 3.x `getBody`, nullable `versionName`, `onSharedPreferenceChanged(key: String?)`, `ViewModelProviders` → `ViewModelProvider`, added SwipeRefreshLayout dependency, `lowercase()`
- [x] **Debug build compiles, installs and launches on the API 25 test device without crashing.** Not tested beyond launching.

Remaining:
- [x] Rewrite `README.md`: Reborn intro, homage and attribution, install from GitHub Releases, "ARCHIVED" notice removed
- [x] Add a `NOTICE` file (Apache 2.0 attribution to quadportnick / ThanksMister / TheTimeWalker)
- [x] Remove `CNAME` (wallpanel.xyz) and `.github/FUNDING.yml` (pointed at TheTimeWalker); update `_config.yml`
- [x] Website: Docusaurus config moved to `beecho01.github.io/wallpanel-reborn/`; getting started, limitations and privacy policy rewritten (no Firebase, contact via GitHub Issues). Builds locally.
- [x] GitHub Actions: `android.yml` builds debug APKs on push/PR and, on a `v*` tag, attaches them to a prerelease; `website.yml` deploys the site to Pages
- [x] Manifest cleanup: removed the redundant `com.google.android.gms.version` meta-data
- [x] `gradlew` marked executable in git (needed for Linux CI)
- [ ] Rename the GitHub repo `Wallpanel-Reborn` → `wallpanel-reborn`, update `origin`, and set Settings → Pages → Source to "GitHub Actions"
- [ ] New adaptive launcher icon (keep the panel/window motif) plus a monochrome themed icon. The same artwork is also `assets/icon.png`, used by `error_page.html` (shown by `InternalWebClient` when a page fails to load), so replace it there too.
- [ ] Clean up the build warnings (deprecations) as files are touched
- [ ] Release signing (deferred; debug-signed APKs for now)

### Phase 1b: targetSdk 33 → 36

Behaviour changes to handle: foreground service types (34), `registerReceiver` exported flags (34), forced edge-to-edge (35/36), predictive back, notification permission (33). Best done alongside or just before the Compose work.

## Phase 2: Bundled / loadable WebView (key feature; upstream issue #149)

Goal: lift old devices (such as the API 25 test device stuck on WebView 52) to the newest Chromium their OS supports.

Newest Chromium per Android release:

| Android | API | Newest Chromium WebView |
|---|---|---|
| 7.0–7.1 | 24–25 | **119** |
| 8.0–9 | 26–28 | **138** |
| 10+ | 29+ | current |

Approach:
- Loader: [JonaNorman/WebViewUpgrade](https://github.com/JonaNorman/WebViewUpgrade) (Apache 2.0, minSdk 21, active as of September 2026). It loads a WebView APK into the app's own process and leaves the system untouched. It's only tested on Android 10–14, so **a spike on the API 25 device comes first**.
- Pick the tier from `Build.VERSION.SDK_INT` and ABI. Only use the bundled engine if it's newer than the system one (`WebViewCompat.getCurrentWebViewPackage`).
- **Source of APKs:** must be an open-source Chromium `system_webview_apk` (`com.android.webview`). Google's `com.google.android.webview` is proprietary and can't be redistributed. Options: find trustworthy prebuilt open-source builds, or build Chromium ourselves (heavy, and probably too big for free CI). **This is the main open question.**
- Distribution: each APK is ~100MB+ per ABI, so don't bundle them all. Download on demand from our own GitHub Releases (verified against a pinned SHA-256 checksum), plus a "load from file" import for offline devices. Optionally later, an offline app variant with 119 arm64 built in.
- Settings: engine choice (System / Bundled / Custom file), a version readout, and a warning when the system WebView is outdated. Changing engine needs an app restart.
- Security note: 119 is frozen (2023 CVEs). Show a notice if the start URL is a public website.

Steps:
1. Spike: a throwaway build that loads a Chromium 119 arm64 WebView APK on the test device and renders a Home Assistant dashboard.
2. If it works: tier selection, download and checksum, file import, settings UI and version readout.

## Phase 3: Quick wins (upstream issues)

- Security: #145 (MQTT opens settings without the PIN), #119 (crash when tapping the security code)
- WebView gaps: #71/#172 (intents and app links), #126 (fullscreen video), #85 (microphone), #155 (geolocation), #158 (cookies), #163/#73 (Grafana), #168 (client certificates), #169 (HTTP basic auth)
- Small features: #79 (24h clock), #43 (bigger clock), #72 (clear cache), #141/#112 (settings import/export), #30 (rotate/flip camera), #6 (stream resolution), #20/#28 (auto refresh), #38 (dims while in settings), #87/#146 (screensaver over MQTT/API), #138 (dark mode over MQTT)

## Phase 4: Compose + Material 3 UI

- The WebView stays a WebView (`AndroidView` wrapper). Everything around it moves to Compose: settings (9 preference screens, replacing PreferenceFragment, possibly moving to DataStore), the clock/screensaver, the PIN keypad/code sheet, the welcome screen and About.
- Dagger-android → Hilt; RxJava/LiveData → coroutines/Flow.

## Phase 5: Reliability, camera and MQTT

- Camera: deprecated `play-services-vision` and the old Camera API → **CameraX + ML Kit**. This doesn't work on devices without Play Services, such as the AOSP test device, so consider bundled ML Kit models.
- Reliability: #11, #39, #50, #136, #134, #19, #18 (boot), #40/#110/#125/#131/#42 (detection stops). Needs a proper foreground service and watchdog.
- MQTT: #171, #165, #157, #115, #114, #98, #86, #80

## Deferred / out of scope

#132 (Android TV), #161 (Samsung DeX), #139 (SMS), #120 (voice assistant), #127 (Daydream), #83 (Amazon Appstore). F-Droid (#14) becomes possible now that Firebase is gone.
Not actionable: #174, #140 and plain questions.
