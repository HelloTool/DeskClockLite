# Desk Clock Lite

![GitHub](https://img.shields.io/github/license/Jesse205/Desk-Clock-Lite)

![哈兔 Box 功能](https://img.shields.io/badge/%E5%93%88%E5%85%94%20Box-%E5%8A%9F%E8%83%BD-blue)
![Edde 学习系列](https://img.shields.io/badge/Edde%20%E5%AD%A6%E4%B9%A0-%E7%B3%BB%E5%88%97-blue)

[简体中文](./README.zh.md) |
**English**

This is my second Android application written in Java.

The purpose of making it is to use waste. It is backward compatible with Android 2.3 and can be used on many Android phones.

Some codes may not be implemented well. I hope you can tell me in issues.

For a bright theme, you can press and hold the background to enable it.

For devices with a navigation bar under Android 4.4, you can tap the background to hide the navigation bar.

This repositories mainly uses [GitLab](https://gitlab.com/Jesse205/Desk-Clock-Lite/), [Github](https://github.com/Jesse205/Desk-Clock-Lite) as the image, and there is no Gitee image.

## Download

- [GitLab Release](https://gitlab.com/Jesse205/Desk-Clock-Lite/-/releases)

## Style Specification

- Color switching: long press background
- Time:
  - Position: centered relative to the screen
  - Font size: follow the screen size, or `0.225f * screen width + 0.5f` if the device doesn't support it.
  - Format: `HH:mm:ss` (can be localized but without AM, PM)
  - Margins: `Width * 0.1`
- Date:
  - Position: bottom
  - Format: `EEEE, MMMM dd` (can be localized but without year)
  - Other attributes:

| Screen width    | Font size | Margin |
| --------------- | --------- | ------ |
| 1600dp          | 48dp      | 56dp   |
| 1280dp          | 34dp      | 40dp   |
| 800dp           | 20dp      | 24dp   |
| 600dp（Tablet） | 20dp      | 24dp   |
| 400dp           | 18dp      | 16dp   |
| 320dp           | 18dp      | 16dp   |
| 240dp（Phone）  | 16dp      | 16dp   |
| 0dp（Watch）    | 12dp      | 48dp   |

## Thanks

- **[IconPark](https://iconpark.oceanengine.com/official)**: App icon

## Communicate

- QQ Group - [Edde 学习桌交流群](https://jq.qq.com/?_wv=1027&k=xBZAOI2D)
- QQ Group - [Edde 综合交流群](https://jq.qq.com/?_wv=1027&k=54XFVLSq)
