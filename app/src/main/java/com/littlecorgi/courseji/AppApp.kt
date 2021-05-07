package com.littlecorgi.courseji

import android.util.Log
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.littlecorgi.commonlib.App
import com.littlecorgi.courseji.utils.TTAdManagerHolder
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.inapp.InAppMessageManager
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.xiaomi.MiPushRegistar

/**
 * 自定义的Application。继承自commonlib库中的App。
 * 本质上主要的Application中初始化内容是在commonlib的App中的，但是由于友盟的问题，只能在此初始化，
 * 所以此类目前来说主要的作用就是承接友盟的初始化，而其他的初始化或者与Application类相关的逻辑代码都在commonlib.App中
 * @author littlecorgi 2020/11/17
 */
class AppApp : App() {

    override fun onCreate() {
        super.onCreate()
        initUMengPush()

        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        // 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        // 包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)

        // 初始化穿山甲广告SDK
        TTAdManagerHolder.init(this)

        Log.d("UMengInitializer", "onCreate: APP APP初始化了")
    }

    private fun initUMengPush() {
        UMConfigure.init(
            this, "5f9a8e3e1c520d30739bfe55", "Umeng",
            UMConfigure.DEVICE_TYPE_PHONE, "7f70e2dc06073c2988d4d26f6eeee1b5"
        )
        // 获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(this)

        // 应用内消息测试模式，线上时注释掉此代码
        InAppMessageManager.getInstance(this).setInAppMsgDebugMode(!isDebug)

        // 自检
        mPushAgent.isPushCheck = false

        // 打印当前Resource包名
        Log.i("UMengInitializer", "create: resourcePackageName: ${mPushAgent.resourcePackageName}")

        // 设置资源包名
        // mPushAgent.resourcePackageName = "com.littlecorgi.commonlib"
        // mPushAgent.resourcePackageName = "com.littlecorgi.courseji"

        // mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //服务端控制声音

        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                // 注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i("UMengInitializer", "注册成功：deviceToken：-------->  $deviceToken")
            }

            override fun onFailure(s: String, s1: String) {
                Log.e("UMengInitializer", "注册失败：-------->  s:$s,s1:$s1")
            }
        })

        // 再次打印当前Resource包名
        Log.i("UMengInitializer", "create: resourcePackageName: ${mPushAgent.resourcePackageName}")

        /**
         * 初始化厂商通道
         */
        // 小米通道
        MiPushRegistar.register(this, "2882303761518784162", "5681878493162")
        // 华为通道，注意华为通道的初始化参数在manifest中配置
        HuaWeiRegister.register(this)
        // 魅族通道
        // MeizuRegister.register(this, "填写您在魅族后台APP对应的app id", "填写您在魅族后台APP对应的app key");
        // OPPO通道
        // OppoRegister.register(this, "填写您在OPPO后台APP对应的app key", "填写您在魅族后台APP对应的app secret");
        // VIVO 通道，注意VIVO通道的初始化参数在minifest中配置
        // VivoRegister.register(this);
    }
}
