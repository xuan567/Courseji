package com.littlecorgi.courseji.runalone

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.lifecycle.lifecycleScope
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.littlecorgi.commonlib.BaseActivity
import com.littlecorgi.commonlib.context.start
import com.littlecorgi.courseji.AppDatabase
import com.littlecorgi.courseji.R
import com.littlecorgi.courseji.schedule.logic.model.bean.TableBean
import com.littlecorgi.courseji.schedule.logic.model.bean.TimeDetailBean
import com.littlecorgi.courseji.schedule.logic.model.bean.TimeTableBean
import com.littlecorgi.courseji.utils.TTAdManagerHolder
import com.littlecorgi.courseji.utils.getPrefer
import kotlinx.coroutines.launch

/**
 * 组件化单独运行时的SplashActivity
 *
 * @author littlecorgi 2021/05/07
 */
class SplashActivity : BaseActivity() {

    private lateinit var mSplashContainer: FrameLayout
    private lateinit var mTTAdNative: TTAdNative
    private val mIsExpress = false // 是否请求模板广告

    // private val myAppContextPresenter: MyAppContextPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

        mSplashContainer = findViewById(R.id.splash_container)
        // step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get()!!.createAdNative(this)

        // step3:加载广告
        loadSplashAd()

        if (!applicationContext.getPrefer().getBoolean("has_adjust", false)) {
            lifecycleScope.launch {
                val tableData = TableBean(type = 1, id = 1, tableName = "")
                val dataBase = AppDatabase.getDatabase(applicationContext)
                val tableDao = dataBase.tableDao()
                val timeDao = dataBase.timeDetailDao()
                val timeTableDao = dataBase.timeTableDao()
                if (timeTableDao.getTimeTable(1) == null) {
                    timeTableDao.insertTimeTable(TimeTableBean(id = 1, name = "默认"))
                }
                val timeList = ArrayList<TimeDetailBean>().apply {
                    add(TimeDetailBean(1, "08:00", "08:50", 1))
                    add(TimeDetailBean(2, "08:55", "09:45", 1))
                    add(TimeDetailBean(3, "10:15", "11:05", 1))
                    add(TimeDetailBean(4, "11:10", "12:00", 1))
                    add(TimeDetailBean(5, "14:30", "15:20", 1))
                    add(TimeDetailBean(6, "15:25", "16:15", 1))
                    add(TimeDetailBean(7, "16:30", "17:20", 1))
                    add(TimeDetailBean(8, "17:25", "18:15", 1))
                    add(TimeDetailBean(9, "19:00", "19:50", 1))
                    add(TimeDetailBean(10, "19:55", "20:45", 1))
                }
                try {
                    timeDao.insertTimeList(timeList)
                    tableDao.insertTable(tableData)
                } catch (e: Exception) {
                }
                applicationContext.getPrefer().edit()
                    .putBoolean("has_adjust", true)
                    .putInt("", 1)
                    .apply()
            }
        }
    }

    private inline fun <reified A : Activity> startToActivity() {
        start<A>()
        // 销毁此Activity，以便在MainActivity返回时不再会显示此Activity
        this.finish()
    }

    /**
     * 加载开屏广告
     */
    private fun loadSplashAd() {
        // step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = if (mIsExpress) {
            // 个性化模板广告需要传入期望广告view的宽、高，单位dp，请传入实际需要的大小，
            // 比如：广告下方拼接logo、适配刘海屏等，需要考虑实际广告大小
            // float expressViewWidth = UIUtils.getScreenWidthDp(this);
            // float expressViewHeight = UIUtils.getHeight(this);
            AdSlot.Builder()
                .setCodeId("887444670") // 模板广告需要设置期望个性化模板广告的大小,单位dp,代码位是否属于个性化模板广告，请在穿山甲平台查看
                // view宽高等于图片的宽高
                .setExpressViewAcceptedSize(1080f, 1920f)
                .build()
        } else {
            AdSlot.Builder()
                .setCodeId("887444670")
                .setImageAcceptedSize(1080, 1920)
                .build()
        }

        // step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(
            adSlot,
            object : TTAdNative.SplashAdListener {
                @MainThread
                override fun onError(code: Int, message: String) {
                    Log.d(TAG, message)
                    showErrorToast(this@SplashActivity, message)
                    startToActivity<MainActivity>()
                }

                @MainThread
                override fun onTimeout() {
                    showErrorToast(this@SplashActivity, "开屏广告加载超时")
                    startToActivity<MainActivity>()
                }

                @MainThread
                override fun onSplashAdLoad(ad: TTSplashAd) {
                    Log.d(TAG, "开屏广告请求成功")
                    // 获取SplashView
                    val view = ad.splashView
                    if (!this@SplashActivity.isFinishing) {
                        mSplashContainer.removeAllViews()
                        // 把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                        mSplashContainer.addView(view)
                        // 设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                        // ad.setNotAllowSdkCountdown();
                    } else {
                        startToActivity<MainActivity>()
                    }

                    // 设置SplashView的交互监听器
                    ad.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                        override fun onAdClicked(view: View, type: Int) {
                            Log.d(TAG, "onAdClicked")
                            showInfoToast(this@SplashActivity, "开屏广告点击")
                        }

                        override fun onAdShow(view: View, type: Int) {
                            Log.d(TAG, "onAdShow")
                            // showInfoToast(this@SplashActivity, "开屏广告展示")
                        }

                        override fun onAdSkip() {
                            Log.d(TAG, "onAdSkip")
                            // showInfoToast(this@SplashActivity, "开屏广告跳过")
                            startToActivity<MainActivity>()
                        }

                        override fun onAdTimeOver() {
                            Log.d(TAG, "onAdTimeOver")
                            // showInfoToast(this@SplashActivity, "开屏广告倒计时结束")
                            startToActivity<MainActivity>()
                        }
                    })
                    if (ad.interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                        ad.setDownloadListener(object : TTAppDownloadListener {
                            var hasShow = false
                            override fun onIdle() {}
                            override fun onDownloadActive(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                                if (!hasShow) {
                                    showInfoToast(this@SplashActivity, "下载中...")
                                    hasShow = true
                                }
                            }

                            override fun onDownloadPaused(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                                showInfoToast(this@SplashActivity, "下载暂停...")
                            }

                            override fun onDownloadFailed(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                                showErrorToast(this@SplashActivity, "下载失败...")
                            }

                            override fun onDownloadFinished(
                                totalBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                                showInfoToast(this@SplashActivity, "下载完成...")
                            }

                            override fun onInstalled(fileName: String, appName: String) {
                                showInfoToast(this@SplashActivity, "安装完成...")
                            }
                        })
                    }
                }
            },
            AD_TIME_OUT
        )
    }

    companion object {
        const val AD_TIME_OUT = 5 * 1000
        const val TAG = "Splash[runalone]"
    }
}