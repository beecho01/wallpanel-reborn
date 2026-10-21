# WallPanel Reborn

WallPanel Reborn is an Android app for web-based dashboards and home automation platforms such as Home Assistant. It turns a wall-mounted tablet or photo frame into an always-on control panel.

It continues [WallPanel](https://github.com/TheTimeWalker/wallpanel-android) by ThanksMister and TheTimeWalker, which was archived in 2025. Reborn modernises the build and targets current Android, while still supporting old wall-panel hardware.

## Install

Download the latest APK from [GitHub Releases](https://github.com/beecho01/wallpanel-reborn/releases) and sideload it. For most devices, use the `arm64-v8a` APK. If you're not sure which one you need, use the `universal` APK.

WallPanel Reborn has a different application ID (`io.github.beecho01.wallpanelreborn`) from the original app, so you can install both side by side. Settings don't carry over.

## Documentation

See the [WallPanel Reborn documentation](https://beecho01.github.io/wallpanel-reborn/) for setup, MQTT and HTTP commands, sensors, the screensaver and camera streaming.

## Screenshots

<img src="img/dashboard2.png" width="640" />
<img src="img/dashboard3.png" width="640" />
<img src="img/dashboard1.png" width="640" />

## Features

- Shows any web-based dashboard or home automation front end full screen
- Can optionally be set as the Android home screen (kiosk use)
- Settings protected by a code, with a settings button that can be hidden
- Camera support for streaming video, motion detection, face detection and QR code reading
- Text-to-speech for notification messages over MQTT or HTTP
- Remote control over MQTT or HTTP (URL, brightness, wake and more)
- Reports device sensor data (temperature, light, pressure, battery)
- MJPEG streaming server using the device camera
- Screensaver that can be dismissed by motion or face detection
- Opens external apps using intent URLs

## Requirements

- Android 7.0 (API 24) or newer
- Pages render using the device's Android System WebView, which is not the same as Chrome. On old devices with an outdated WebView, modern dashboards may not render correctly. A future release will be able to load a newer WebView engine on these devices.

## Building

Requirements:

- JDK 21 (for example the JBR that ships with Android Studio)
- Android SDK with platform `android-37` and build-tools `37.0.0`

Create a `local.properties` file in the project root that points to your SDK:

```properties
sdk.dir=C:/Users/<you>/AppData/Local/Android/Sdk
```

Optionally, add `code`, `hassUrl`, `broker`, `brokerUsername` and `brokerPass` to prefill debug builds.

Then build:

```sh
./gradlew :WallPanelApp:assembleDebug
```

The APKs are written to `WallPanelApp/build/outputs/apk/debug/`.

## Contributing

Bug reports and feature requests are welcome in the [issue tracker](https://github.com/beecho01/wallpanel-reborn/issues). Pull requests are welcome too.

## Credits

WallPanel Reborn is a continuation of WallPanel, built as an homage to the original:

- [quadportnick](https://github.com/quadportnick), who started [the original WallPanel (formerly HomeDash)](https://github.com/WallPanel-Project/wallpanel-android)
- [ThanksMister](https://github.com/thanksmister), who maintained and developed [WallPanel](https://github.com/thanksmister/wallpanel-android) for many years
- [TheTimeWalker](https://github.com/TheTimeWalker), who maintained [WallPanel](https://github.com/TheTimeWalker/wallpanel-android) from 2022 until it was archived
- Everyone who [contributed](https://github.com/TheTimeWalker/wallpanel-android/graphs/contributors) to the original project

## Licence

Apache License 2.0. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
