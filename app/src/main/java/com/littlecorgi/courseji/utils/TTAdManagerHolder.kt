package com.littlecorgi.courseji.utils

import android.content.Context
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdManager
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.littlecorgi.commonlib.App

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 * @author littlecorgi 2021/3/5
 */
object TTAdManagerHolder {

    private const val TAG = "TTAdManagerHolder"

    private var sInit = false

    fun get(): TTAdManager? {
        if (!sInit) {
            throw RuntimeException("TTAdSdk is not init, please check.")
        }
        return TTAdSdk.getAdManager()
    }

    fun init(context: Context) {
        doInit(context)
    }

    // step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private fun doInit(context: Context) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context))
            sInit = true
        }
    }

    private fun buildConfig(context: Context): TTAdConfig? {
        return TTAdConfig.Builder()
            .appId("5148849")
            .useTextureView(true) // 默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
            .appName("Courseji")
            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK) // 落地页主题
            .allowShowNotify(true) // 是否允许sdk展示通知栏提示
            .debug(App.isDebug) // 测试阶段打开，可以通过日志排查问题，上线时去除该调用
            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) // 允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
            .supportMultiProcess(false) // 是否支持多进程，true支持
            .asyncInit(false) // 是否异步初始化sdk,设置为true可以减少SDK初始化耗时。3450版本开始废弃~~
            .build()
    }
}
