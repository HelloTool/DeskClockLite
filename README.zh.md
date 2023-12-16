# 桌面时钟 Lite

![GitHub](https://img.shields.io/github/license/Jesse205/Desk-Clock-Lite)

![哈兔 Box 功能](https://img.shields.io/badge/%E5%93%88%E5%85%94%20Box-%E5%8A%9F%E8%83%BD-blue)
![Edde 学习系列](https://img.shields.io/badge/Edde%20%E5%AD%A6%E4%B9%A0-%E7%B3%BB%E5%88%97-blue)

**简体中文** |
[English](./README.md)

这是我第二个使用 Java 编写的安卓软件

编写这个软件的目的是利用旧手机。软件向前兼容至 Android 2.3，可能也兼容 Android
2.2，因此您可以在您几乎所有的安卓设备上安装它（前提是您的设备支持安装第三方软件）。

一些代码可能实现得不是那么的好。希望您可以在 issues 内纠正我一些问题。

对于亮色模式，您可以长按背景启用它。

对于搭载 Android 4.4 以下且拥有导航栏的设备，您可以点击背景将导航栏隐藏。

本仓库以 [GitLab](https://gitlab.com/Jesse205/Desk-Clock-Lite/)
为主， [Github](https://github.com/Jesse205/Desk-Clock-Lite) 为镜像，暂无 Gitee 镜像。

## 下载

- [GitLab Release](https://gitlab.com/Jesse205/Desk-Clock-Lite/-/releases)

## 样式规范

- 颜色切换：长按背景
- 时间：
  - 位置：相对于屏幕居中
  - 字体大小：跟随屏幕大小变化，如果设备不支持则为 `0.225f * 屏幕宽度 + 0.5f`。
  - 格式：`HH:mm:ss`（可以本地化，不带上午、下午）
  - 边距：`宽度 * 0.1`
- 日期：
  - 位置：底部
  - 格式：`MM月dd日 EEEE`（可以本地化，不带年份）
  - 其他属性：
  
| 屏幕宽度      | 字体大小 | 边距 |
| ------------- | -------- | ---- |
| 1600dp        | 48dp     | 56dp |
| 1280dp        | 34dp     | 40dp |
| 800dp         | 20dp     | 24dp |
| 600dp（平板） | 20dp     | 24dp |
| 400dp         | 18dp     | 16dp |
| 320dp         | 18dp     | 16dp |
| 240dp（手机） | 16dp     | 16dp |
| 0dp（手表）   | 12dp     | 48dp |

## 系列项目

- [桌面时钟 Lite for OpenHarmony](https://gitee.com/Jesse205/DeskClockLiteForOpenHarmony)（Gitee）
- [桌面时钟 Lite for Android](https://gitlab.com/Jesse205/Desk-Clock-Lite)（GitLab）
- [桌面时钟 Lite for 浏览器](https://gitee.com/Jesse205/Jesse205/tree/master/deskclocklite)（Gitee）

## 感谢

- **[IconPark](https://iconpark.oceanengine.com/official)**: 软件图标

## 交流

- QQ 群 - [Edde 学习桌交流群](https://jq.qq.com/?_wv=1027&k=xBZAOI2D)
- QQ 群 - [Edde 综合交流群](https://jq.qq.com/?_wv=1027&k=54XFVLSq)
