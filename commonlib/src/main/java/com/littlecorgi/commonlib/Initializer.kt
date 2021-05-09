package com.littlecorgi.commonlib

import android.app.Application
import android.content.Context
import android.graphics.Typeface
import android.os.Process
import android.util.Log
import androidx.startup.Initializer
import com.alibaba.android.arouter.launcher.ARouter
import com.littlecorgi.commonlib.App.Companion.isDebug
import com.littlecorgi.commonlib.util.getProcessName
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import es.dmoral.toasty.Toasty

/**
 * APP Startup 的 Initializer 类
 * @author littlecorgi 2020/10/19
 */

/**
 * 初始化 Toasty，配置为自动初始化
 */
class ToastyInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Toasty.Config.getInstance()
            .tintIcon(true) // 图标着色
            .setToastTypeface(Typeface.DEFAULT_BOLD) // 字体粗度
            .setTextSize(12) // 字体大小
            .allowQueue(true) // 防止多个toast排队
            .apply()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

/**
 * 初始化 ARouter
 */
class ARouterInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (isDebug) { // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(context as Application) // 尽可能早，推荐在Application中初始化
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

/**
 * 初始化 Bugly
 */
class BuglyInitializer : Initializer<Unit> {
    override fun create(context: Context) {

        // 多进程时，每个进程默认都会初始化bugly，避免这种情况发生，设置为只有主进程才数据上报
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName

        // 监听APP升级状态
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgradeFailed(b: Boolean) {
                Log.d("BuglyInitializer", "upgradeStateListener upgrade fail")
            }

            override fun onUpgradeSuccess(b: Boolean) {
                Log.d("BuglyInitializer", "upgradeStateListener upgrade success")
            }

            override fun onUpgradeNoVersion(b: Boolean) {
                Log.d("BuglyInitializer", "upgradeStateListener upgrade has no new version")
            }

            override fun onUpgrading(b: Boolean) {
                Log.d("BuglyInitializer", "upgradeStateListener upgrading")
            }

            override fun onDownloadCompleted(b: Boolean) {
                Log.d("BuglyInitializer", "upgradeStateListener download apk file success")
            }
        }

        /**
         * 设置这个是为了防止在SplashActivity倒计时还没结束时就检测到升级进而导致升级页面的Activity无法正常显示
         * 会手动在MainActivity中调用Beta.checkUpgrade()
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = false

        /**
         * 此方法不行，因为commonlib中检查不到MainActivity这个类
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        // Beta.canShowUpgradeActs.add(MainActivity::class)

        // 初始化Bugly
        // 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
        // - 输出详细的Bugly SDK的Log；
        // - 每一条Crash都会被立即上报；
        // - 自定义日志将会在Logcat中输出：true为输出，false为不输出，建议在测试阶段建议设置成true，发布时设置为false。
        // CrashReport.initCrashReport(context, "28d25075a1", isDebug)
        // 接入热修复后使用此方法
        Bugly.init(context, "28d25075a1", isDebug)

        // 如果需要测试崩溃，可以模拟设置一个按钮调用以下代码，APP就会崩溃，等待一会(应该不会超过1min)，就能在bugly后台看到信息了
        // CrashReport.testJavaCrash()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

// /**
//  * 初始化 LeakCanary ，配置为手动初始化
//  */
// class LeakCanaryInitializer : Initializer<Unit> {
//     override fun create(context: Context) {
//         val application = context.applicationContext as Application
//         AppWatcher.manualInstall(application)
//     }
//
//     override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
// }
