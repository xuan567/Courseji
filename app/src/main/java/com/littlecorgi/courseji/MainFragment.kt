package com.littlecorgi.courseji

import android.animation.StateListAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.littlecorgi.commonlib.BaseFragment
import com.littlecorgi.commonlib.context.startActivityForResult
import com.littlecorgi.commonlib.util.TimeUtil
import com.littlecorgi.commonlib.util.colorSL
import com.littlecorgi.commonlib.util.dip
import com.littlecorgi.courseji.databinding.FragmentMainBinding
import com.littlecorgi.courseji.schedule.ui.ScheduleViewPagerFragmentStateAdapter
import com.littlecorgi.courseji.schedule.vm.ScheduleViewModel
import com.littlecorgi.courseji.schedule_import.ui.ChooseImportFragment
import com.littlecorgi.courseji.schedule_setting.ui.ScheduleSettingsActivity
import com.littlecorgi.courseji.utils.Const
import com.littlecorgi.courseji.utils.CourseUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException

@Route(path = "/app/fragment_main")
class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mBinding: FragmentMainBinding
    private lateinit var mViewModel: ScheduleViewModel
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mWeekToggleGroup: MaterialButtonToggleGroup
    private lateinit var mVPAdapter: ScheduleViewPagerFragmentStateAdapter

    private val mScheduleViewPagerScrollerListener = object : ViewPager2.OnPageChangeCallback() {
        // 当课程表滑动时
        override fun onPageSelected(position: Int) {
            mViewModel.selectedWeek = position + 1
            try {
                if (mViewModel.currentWeek > 0) {
                    if (mViewModel.selectedWeek == mViewModel.currentWeek) {
                        val text = "第${mViewModel.selectedWeek}周"
                        mBinding.tvWeek.text = text
                        mBinding.tvWeekday.text = CourseUtils.getWeekday()
                    } else {
                        val text = "第${mViewModel.selectedWeek}周"
                        mBinding.tvWeek.text = text
                        mBinding.tvWeekday.text = "非本周"
                    }
                } else {
                    val text = "第${mViewModel.selectedWeek}周"
                    mBinding.tvWeek.text = text
                    mBinding.tvWeekday.text = "还没有开学哦"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                switchWeekToggleGroupAndAnimation()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        mBottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet)
        mWeekToggleGroup = mBinding.bottomSheetTgWeek
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        // mViewModel = ViewModelProvider(this@MainFragment).get(MainFragmentViewModel::class.java)
        // 初始化View
        initView()
        // 初始化数据
        initData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(ScheduleViewModel::class.java)
        // // 初始化View
        // initView()
        // // 初始化数据
        // initData()
    }

    override fun onActivityCreated() {
        // // 初始化View
        // initView()
        // // 初始化数据
        // initData()
    }

    private fun initView() {
        // todo
        // 此处需要先执行数据库操作，读取到table，之后才能走后面的初始化流程，
        // 但是数据库的读取属于耗时任务，此时会阻塞后面的View操作，所以会造成UI显示暂停1s左右
        initBottomSheet()
        initScheduleViewPager()
        initWeekToggleGroup()

        // 此处两行代码最后就这样，别进行修改，一定得先initEvent()再showTvWeekText()
        // 初始化事件
        initEvent()
        // 根据当前周数显示周数Text，放在此处是为了确保在加载完ViewPager的监听事件后，再去显示周数Text
        // 防止监听事件中的对周数Text的显示的修改对我们此处的显示造成干扰
        showTvWeekText()
        for (i in 1..7) {
            mViewModel.getRawCourseByDay(i, mViewModel.table.id)
                .observe(
                    requireActivity(),
                    Observer { list ->
                        if (list == null) return@Observer
                        if (list.isNotEmpty() && list[0].tableId != mViewModel.table.id) return@Observer
                        mViewModel.allCourseList[i - 1].value = list
                    }
                )
        }
    }

    /**
     * 控制底部BottomSheet
     *  1. 设置more按钮的功能
     *  2. 设置当点击BottomSheet时再自动隐藏
     */
    private fun initBottomSheet() {
        // 默认先隐藏，不显示BottomSheet
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * 初始化课程表ViewPager
     */
    private fun initScheduleViewPager() {
        mVPAdapter =
            ScheduleViewPagerFragmentStateAdapter(requireActivity(), mViewModel.table.maxWeek)
        mBinding.vpSchedule.apply {
            this.adapter = mVPAdapter
            // this.offscreenPageLimit = 1
            currentItem = mViewModel.currentWeek
        }
        mVPAdapter.notifyDataSetChanged()
        if (CourseUtils.countWeek(mViewModel.table.startDate, mViewModel.table.sundayFirst) > 0) {
            mBinding.vpSchedule.currentItem =
                CourseUtils.countWeek(mViewModel.table.startDate, mViewModel.table.sundayFirst) - 1
        } else {
            mBinding.vpSchedule.currentItem = 0
        }
    }

    /**
     * 初始化周数选择组件
     */
    private fun initWeekToggleGroup() {
        // 添加Button
        addWeekToggleGroupButton()
        lifecycleScope.launch {
            delay(1000)
            switchWeekToggleGroupAndAnimation()
        }
    }

    /**
     * 必须在UIThread中进行操作
     */
    @UiThread
    private fun addWeekToggleGroupButton() {
        mWeekToggleGroup.removeAllViews()
        mWeekToggleGroup.clearChecked()
        // 根据周数，总共有几个周就添加几个Button进去
        for (i in 1..mViewModel.table.maxWeek) {
            mWeekToggleGroup.addView(
                MaterialButton(requireContext()).apply {
                    setTextColor(colorSL(R.color.mtrl_text_btn_text_color_selector))
                    val space = dip(8)
                    setPadding(space, 0, space, 0)
                    backgroundTintList = colorSL(R.color.mtrl_btn_text_btn_bg_color_selector)
                    rippleColor = colorSL(R.color.mtrl_btn_text_btn_ripple_color)
                    elevation = 0f
                    stateListAnimator = StateListAnimator()
                }.apply {
                    strokeColor = colorSL(R.color.mtrl_btn_stroke_color_selector)
                    strokeWidth = dip(1)
                }.apply {
                    id = i
                    text = i.toString()
                    textSize = 12f
                },
                requireContext().dip(48), requireContext().dip(48)
            )
        }
    }

    /**
     * 根据currentWeek和maxWeek的信息判断当前是在课程表的周内还是周外，并显示对应信息
     */
    private fun showTvWeekText() {
        if (mViewModel.currentWeek > 0) {
            if (mViewModel.currentWeek <= mViewModel.table.maxWeek) {
                val text = "第${mViewModel.currentWeek}周"
                // 其实此处和上面ViewPager的滚动监听的设置text重复了
                mBinding.tvWeek.text = text
            } else {
                mBinding.tvWeek.text = "当前周已超出设定范围"
                // TODO 完善周数管理逻辑，当超过时可以选择手动设置上学周期
            }
        } else {
            mBinding.tvWeek.text = "还没有开学哦"
        }
    }

    /**
     * 当课程表ViewPager滑动后，再次打开BottomSheetBehavior的时候自动跳转到对应周Button及其滑动动画
     */
    private fun switchWeekToggleGroupAndAnimation() {
        mBinding.bottomSheetSvWeek.smoothScrollTo(
            if (mViewModel.selectedWeek > 4) (mViewModel.selectedWeek - 4) * requireContext().dip(56) else 0,
            0
        )
        if (mWeekToggleGroup.checkedButtonId != mViewModel.selectedWeek) {
            mWeekToggleGroup.check(mViewModel.selectedWeek)
        }
    }

    private fun initData() {
        initHeadImage()
        initBg()
        initWhatDay()
    }

    /**
     * 加载Nav header的图片
     */
    private fun initHeadImage() {
        Glide.with(this)
            .load(mViewModel.mHeaderImageUrl)
            .into(mBinding.nv.getHeaderView(0).findViewById(R.id.iv_header))
    }

    /**
     * 加载背景图片
     */
    private fun initBg() {
        Glide.with(this)
            .load(mViewModel.mBgImageUrl)
            .into(mBinding.ivBg)
    }

    private fun initWhatDay() {
        // 显示 n月n日
        mBinding.tvDate.text = TimeUtil.getTodayDate()
        // 显示 周n
        mBinding.tvWeekday.text = TimeUtil.getTodayWeekDay()
    }

    private fun initEvent() {
        // 点击导航按钮显示NavigationView
        mBinding.tvNav.setOnClickListener {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // 点击导入按钮
        mBinding.tvImport.setOnClickListener {
            ChooseImportFragment().show(requireActivity().supportFragmentManager, "importDialog")
        }

        // 点击more按钮显示BottomSheet
        mBinding.tvMore.setOnClickListener {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // 点击BottomSheet自动隐藏
        // 事先一定需要将behavior_hidden设置为true
        // 见 https://blog.csdn.net/wjr1949/article/details/105447735
        // 额外需要注意的是，我特地将BottomSheet的高度设置成了parent，这样给用户看起来点击的不是BottomSheet，但是实际上就是BottomSheet
        // 来达到点击外部自动隐藏的效果
        mBinding.bottomSheet.setOnClickListener {
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        // BottomSheetBehavior的状态监听
        mBottomSheetBehavior.addBottomSheetCallback(mBottomSheetCallback)

        // 周数选择的点击事件。这个View是BottomSheetBehavior里面的那个
        mWeekToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                mBinding.vpSchedule.currentItem = checkedId - 1
            }
        }

        // 修改当前周的按钮，跳转到课表设置Activity
        mBinding.bottomSheetBtnChangeWeek.setOnClickListener {
            this.startActivityForResult<ScheduleSettingsActivity>(Const.REQUEST_CODE_SCHEDULE_SETTING) {
                this.putExtra("tableData", mViewModel.table)
            }
        }

        // 课程表ViewPager的页面切换事件监听
        mBinding.vpSchedule.registerOnPageChangeCallback(mScheduleViewPagerScrollerListener)
    }

    public override fun onBackPressed(): Boolean {
        return when {
            // 当左侧侧滑栏展开时，按下返回建自动隐藏
            mBinding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            // 当BottomSheet展开时，按下返回建自动隐藏
            mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                true
            }
            // 除去上述结果，按默认返回逻辑返回
            else -> false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Const.REQUEST_CODE_SCHEDULE_SETTING -> initView()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        mBinding.vpSchedule.adapter = null
    }

    override fun onDestroy() {
        mBinding.vpSchedule.unregisterOnPageChangeCallback(mScheduleViewPagerScrollerListener)
        mWeekToggleGroup.clearOnButtonCheckedListeners()
        super.onDestroy()
    }
}
