package com.littlecorgi.commonlib

import android.app.Application
import android.content.Context
import com.littlecorgi.commonlib.context.AppContextRepository
import com.littlecorgi.commonlib.context.AppContextRepositoryImpl
import com.littlecorgi.commonlib.context.MyAppContextPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

/**
 * 重写Application
 * @author littlecorgi 2020/10/19
 */
open class App : Application() {

    override fun onCreate() {
        super.onCreate()

        mApplicationContext = applicationContext

        // Toasty配置
        // 放在了Startup中，具体见 [com.littlecorgi.commonlib.Initializer]

        // LeakCanary手动配置，我想禁止LeakCanary通过他的ContentProvider来初始化，减少ContentProvider
        // 突然发现，release包找不到LeakCanary，所以导致导出release包此处会报错，换回LeakCanary自己的ContentProvider
        // TODO 2020.10.28 之后记得删除
        /*AppInitializer.getInstance(this).initializeComponent(
            LeakCanaryInitializer::class.java
        )*/

        // AppContextKoinModule 用来通过依赖注入注入Application的Context
        val appContextModule = module {
            single<AppContextRepository> { AppContextRepositoryImpl(applicationContext) }
            single { MyAppContextPresenter(get()) }
        }
        // Koin配置
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appContextModule)
        }
    }

    companion object {
        /**
         * 代表是否处于调试状态
         * 对于有些代码我们需要调试和不调试是两种状态
         *  比如
         *      1. Splash页面停3秒
         *      2. 显示一些Debug的Toast等等
         *      3. Bugly的调试开关
         * 对于这些情况我们就需要此开关
         *
         * true代表调试，false代表线上
         */
        @JvmField
        val isDebug: Boolean = BuildConfig.DEBUG

        /**
         * 不要在static里面去获取
         */
        lateinit var mApplicationContext: Context

        // APP版本号
        const val versionCodes = "1.0"
    }
}
