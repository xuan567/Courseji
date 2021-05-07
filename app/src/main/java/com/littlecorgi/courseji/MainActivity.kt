package com.littlecorgi.courseji

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.littlecorgi.commonlib.BaseActivity
import com.littlecorgi.commonlib.BaseFragment
import com.littlecorgi.commonlib.util.dip
import com.littlecorgi.courseji.databinding.ActivityMainBinding
import com.littlecorgi.courseji.schedule.vm.ScheduleViewModel
import com.littlecorgi.courseji.utils.CourseUtils
import com.tencent.bugly.beta.Beta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 集成运行时的MainActivity。此组件单独运行时的MainActivity是[com.littlecorgi.courseji.runalone.MainActivity]
 *
 * @author littlecorgi 2020/10/27
 */
@Route(path = "app/MainActivity")
class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel by viewModels<ScheduleViewModel>()
    private lateinit var mMainFragment: Fragment
    private lateinit var mAttendanceFragment: Fragment
    private lateinit var mMiddleFragment: Fragment
    private lateinit var mLeaveFragment: Fragment
    private lateinit var mMyFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Bugly升级 检测是否有新版本
        Beta.checkUpgrade()

        initViewModelData()

        initFragment()

        mBinding.navigationView.setOnNavigationItemSelectedListener { item ->
            val beginTransaction = supportFragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.home -> {
                    beginTransaction.hide(mAttendanceFragment)
                        .hide(mMiddleFragment)
                        .hide(mLeaveFragment)
                        .hide(mMyFragment)
                        .show(mMainFragment)
                        .commit()
                }
                R.id.attendance -> {
                    beginTransaction.hide(mMainFragment)
                        .hide(mMiddleFragment)
                        .hide(mLeaveFragment)
                        .hide(mMyFragment)
                        .show(mAttendanceFragment)
                        .commit()
                }
                R.id.middle -> {
                    beginTransaction.hide(mMainFragment)
                        .hide(mAttendanceFragment)
                        .hide(mLeaveFragment)
                        .hide(mMyFragment)
                        .show(mMiddleFragment)
                        .commit()
                }
                R.id.leave -> {
                    beginTransaction.hide(mMainFragment)
                        .hide(mAttendanceFragment)
                        .hide(mMiddleFragment)
                        .hide(mMyFragment)
                        .show(mLeaveFragment)
                        .commit()
                }
                R.id.user -> {
                    beginTransaction.hide(mMainFragment)
                        .hide(mAttendanceFragment)
                        .hide(mMiddleFragment)
                        .hide(mLeaveFragment)
                        .show(mMyFragment)
                        .commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun initFragment() {
        mMainFragment = MainFragment()
        mAttendanceFragment =
            ARouter.getInstance().build("/attendance/fragment_attendance").navigation() as Fragment
        mMiddleFragment =
            ARouter.getInstance().build("/middle/fragment_middle_student").navigation() as Fragment
        mLeaveFragment =
            ARouter.getInstance().build("/leave/fragment_student_leave").navigation() as Fragment
        mMyFragment = ARouter.getInstance().build("/my/fragment_my_main").navigation() as Fragment

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_main_activity, mMainFragment)
            .add(R.id.frame_main_activity, mAttendanceFragment)
            .add(R.id.frame_main_activity, mMiddleFragment)
            .add(R.id.frame_main_activity, mLeaveFragment)
            .add(R.id.frame_main_activity, mMyFragment)
        ft.hide(mAttendanceFragment)
            .hide(mMiddleFragment)
            .hide(mLeaveFragment)
            .hide(mMyFragment)
        ft.commit()
    }

    private fun initViewModelData() {
        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.table = mViewModel.getDefaultTable()
            mViewModel.timeList = mViewModel.getTimeList(mViewModel.table.timeTable)
            // 获得当前周数
            mViewModel.currentWeek =
                CourseUtils.countWeek(mViewModel.table.startDate, mViewModel.table.sundayFirst)
            // 设置默认的选中周数
            mViewModel.selectedWeek = mViewModel.currentWeek
            // 设置列表高度
            mViewModel.itemHeight = dip(mViewModel.table.itemHeight)
        }
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            // 必须是继承自 {@link com.littlecorgi.courseji.BaseFragment} 的子类才进行调用
            if (fragment is BaseFragment) {
                if (fragment.onBackPressed()) {
                    return
                }
            }
        }
        super.onBackPressed()
    }
}
