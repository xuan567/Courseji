package com.littlecorgi.courseji.schedule.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.littlecorgi.commonlib.lifecycle.MyFragmentLifecycleCallbacks
import com.littlecorgi.commonlib.util.ViewUtils
import com.littlecorgi.courseji.R
import com.littlecorgi.courseji.databinding.FragmentScheduleBinding
import com.littlecorgi.courseji.schedule.logic.model.bean.CourseBean
import com.littlecorgi.courseji.schedule.logic.model.bean.TableBean
import com.littlecorgi.courseji.schedule.vm.ScheduleViewModel
import com.littlecorgi.courseji.utils.CourseUtils

/**
 * 用RecyclerView实现的课程表，但是发现效果还没有addView的好，并且此处并不能用RecyclerView去实现，
 * 因为这样会导致每一天的课程表是单独滑动的RecyclerView，并不能整体滑动
 * @author littlecorgi 2021/1/31
 */
class ScheduleFragmentBackup : Fragment() {

    private lateinit var mBinding: FragmentScheduleBinding
    private var week = 0
    private var weekDay = 1
    private lateinit var weekDate: List<String>
    private val viewModel by activityViewModels<ScheduleViewModel>()
    private var isLoaded = false
    private lateinit var showCourseNumber: LiveData<Int>

    private lateinit var mRvTime: RecyclerView
    private lateinit var mRvTable1: RecyclerView
    private lateinit var mRvTable2: RecyclerView
    private lateinit var mRvTable3: RecyclerView
    private lateinit var mRvTable4: RecyclerView
    private lateinit var mRvTable5: RecyclerView
    private val mRecyclers = ArrayList<RecyclerView>()
    private lateinit var mTvTime: AppCompatTextView
    private lateinit var mTvTable1: AppCompatTextView
    private lateinit var mTvTable2: AppCompatTextView
    private lateinit var mTvTable3: AppCompatTextView
    private lateinit var mTvTable4: AppCompatTextView
    private lateinit var mTvTable5: AppCompatTextView
    private val mTextViews = ArrayList<TextView>()

    private lateinit var mRvItemList1: ArrayList<ScheduleItem>
    private lateinit var mRvItemList2: ArrayList<ScheduleItem>
    private lateinit var mRvItemList3: ArrayList<ScheduleItem>
    private lateinit var mRvItemList4: ArrayList<ScheduleItem>
    private lateinit var mRvItemList5: ArrayList<ScheduleItem>
    private lateinit var mRvItemLists: ArrayList<ArrayList<ScheduleItem>>
    private lateinit var mRvAdapter1: ScheduleRecyclerAdapter
    private lateinit var mRvAdapter2: ScheduleRecyclerAdapter
    private lateinit var mRvAdapter3: ScheduleRecyclerAdapter
    private lateinit var mRvAdapter4: ScheduleRecyclerAdapter
    private lateinit var mRvAdapter5: ScheduleRecyclerAdapter
    private lateinit var mRvAdapterLists: ArrayList<ScheduleRecyclerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        week = arguments?.getInt("week") ?: 0
        // 监听Fragment生命周期变化
        parentFragmentManager.registerFragmentLifecycleCallbacks(
            MyFragmentLifecycleCallbacks(
                ScheduleFragment::class.java.simpleName + arguments?.getInt("week")
            ),
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weekDay = CourseUtils.getWeekdayInt()
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        initRecycler()
        initData()
        showCourseNumber = viewModel.getShowCourseNumber(week)
        Log.d("ScheduleFragment8888", "onCreateView: 1")
        return mBinding.root
    }

    private fun initData() {
        for (i in 1..5) {
            // viewModel.allCourseList[i - 1].observe(viewLifecycleOwner, {
            //     Log.d("ScheduleFragment8888", "initData: 1")
            //     // initWeekPanel(it, i, viewModel.table, mRvItemLists[i - 1], mRvAdapterLists[i - 1])
            // })
        }
    }

    private fun initWeekPanel(
        data: List<CourseBean>?,
        day: Int,
        table: TableBean,
        list: ArrayList<ScheduleItem>,
        adapter: ScheduleRecyclerAdapter
    ) {
        Log.d(TAG, "initWeekPanel: enter function list.size = ${list.size}")
        if (data == null || data.isEmpty()) return
        for (i in data.indices) {
            val c = data[i]
            Log.d(TAG, "initWeekPanel: $c")

            // // 过期的不显示
            // if (c.endWeek < week) {
            //     continue
            // }

            // 不在当前周
            val isOtherWeek = (week % 2 == 0 && c.type == 1) || (week % 2 == 1 && c.type == 2) ||
                (c.startWeek > week)

            if (!table.showOtherWeekCourse && isOtherWeek) continue

            var isError = false

            val textView = mTextViews[i]

            val strBuilder = StringBuilder()
            if (c.step <= 0) {
                c.step = 1
                isError = true
                // Toasty.info(requireContext(), R.string.error_course_data, Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), R.string.error_course_data, Toast.LENGTH_LONG)
                    .show()
            }
            if (c.startNode <= 0) {
                c.startNode = 1
                isError = true
                // Toasty.info(requireContext(), R.string.error_course_data, Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), R.string.error_course_data, Toast.LENGTH_LONG)
                    .show()
            }
            if (c.startNode > table.nodes) {
                c.startNode = table.nodes
                isError = true
                // Toasty.info(requireContext(), R.string.error_course_node, Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), R.string.error_course_node, Toast.LENGTH_LONG)
                    .show()
            }
            if (c.startNode + c.step - 1 > table.nodes) {
                c.step = table.nodes - c.startNode + 1
                isError = true
                // Toasty.info(requireContext(), R.string.error_course_node, Toast.LENGTH_LONG).show()
                Toast.makeText(requireContext(), R.string.error_course_node, Toast.LENGTH_LONG)
                    .show()
            }

            if (c.color.isEmpty()) {
                c.color =
                    "#${
                        Integer.toHexString(
                            ViewUtils.getCustomizedColor(
                                requireActivity(),
                                c.id % 9
                            )
                        )
                    }"
            }

            strBuilder.append(c.courseName)

            if (c.room != "") {
                strBuilder.append("\n@${c.room}")
            }

            if (isOtherWeek) {
                when (c.type) {
                    1 -> strBuilder.append("\n单周")
                    2 -> strBuilder.append("\n双周")
                }
                strBuilder.append("[非本周]")
                textView.visibility = View.VISIBLE
            } else {
                when (c.type) {
                    1 -> strBuilder.append("\n单周")
                    2 -> strBuilder.append("\n双周")
                }
            }

            textView.apply {
                for (node in ((c.startNode + 1) / 2)..((c.startNode + c.step) / 2))
                // list[node - 1] = ScheduleItem(
                //     adapter.setData(
                    adapter.notifyItemChanged(
                        node - 1,
                        ScheduleItem(
                            strBuilder.toString(),
                            table.itemTextSize.toFloat(),
                            table.courseTextColor,
                            Color.parseColor(c.color)
                        )
                    )
            }
        }
        // adapter.notifyDataSetChanged()
        Log.d(TAG, "initWeekPanel: $list")
    }

    private fun initRecycler() {
        mRvTime = mBinding.rvTime
        val size = viewModel.timeList.size
        Log.d(TAG, "initRecycler: size = $size")
        mRvTable1 = mBinding.rvTable1.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvItemList1 = ArrayList(size)
            repeat(size) {
                mRvItemList1.add(ScheduleItem("1"))
            }
            mRvAdapter1 = ScheduleRecyclerAdapter(requireContext(), mRvItemList1)
            this.adapter = mRvAdapter1
            mRvItemList1.forEach {
                Log.d(TAG, "initRecycler: mRvItemList1 $it")
            }
        }
        mRvTable2 = mBinding.rvTable2.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvItemList2 = ArrayList(size)
            repeat(size) {
                mRvItemList2.add(ScheduleItem("1"))
            }
            mRvAdapter2 = ScheduleRecyclerAdapter(requireContext(), mRvItemList2)
            this.adapter = mRvAdapter2
            mRvItemList2.forEach {
                Log.d(TAG, "initRecycler: mRvItemList2 $it")
            }
        }
        mRvTable3 = mBinding.rvTable3.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvItemList3 = ArrayList(size)
            repeat(size) {
                mRvItemList3.add(ScheduleItem("1"))
            }
            mRvAdapter3 = ScheduleRecyclerAdapter(requireContext(), mRvItemList3)
            this.adapter = mRvAdapter3
            mRvItemList3.forEach {
                Log.d(TAG, "initRecycler: mRvItemList3 $it")
            }
        }
        mRvTable4 = mBinding.rvTable4.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvItemList4 = ArrayList(size)
            repeat(size) {
                mRvItemList4.add(ScheduleItem("1"))
            }
            mRvAdapter4 = ScheduleRecyclerAdapter(requireContext(), mRvItemList4)
            this.adapter = mRvAdapter4
            mRvItemList4.forEach {
                Log.d(TAG, "initRecycler: mRvItemList4 $it")
            }
        }
        mRvTable5 = mBinding.rvTable5.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvItemList5 = ArrayList(size)
            repeat(size) {
                mRvItemList5.add(ScheduleItem("1"))
            }
            mRvAdapter5 = ScheduleRecyclerAdapter(requireContext(), mRvItemList5)
            adapter = mRvAdapter5
            mRvItemList5.forEach {
                Log.d(TAG, "initRecycler: mRvItemList5 $it")
            }
        }
        mRvItemLists =
            arrayListOf(mRvItemList1, mRvItemList2, mRvItemList3, mRvItemList4, mRvItemList5)
        mRvAdapterLists =
            arrayListOf(mRvAdapter1, mRvAdapter2, mRvAdapter3, mRvAdapter4, mRvAdapter5)
        mRecyclers.add(mRvTable1)
        mRecyclers.add(mRvTable2)
        mRecyclers.add(mRvTable3)
        mRecyclers.add(mRvTable4)
        mRecyclers.add(mRvTable5)
        mTvTime = mBinding.tvTime
        mTvTable1 = mBinding.tvTable1
        mTvTable2 = mBinding.tvTable2
        mTvTable3 = mBinding.tvTable3
        mTvTable4 = mBinding.tvTable4
        mTvTable5 = mBinding.tvTable5
        mTextViews.add(mTvTable1)
        mTextViews.add(mTvTable2)
        mTextViews.add(mTvTable3)
        mTextViews.add(mTvTable4)
        mTextViews.add(mTvTable5)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weekDate = CourseUtils.getDateStringFromWeek(
            CourseUtils.countWeek(
                viewModel.table.startDate,
                viewModel.table.sundayFirst
            ),
            week, viewModel.table.sundayFirst
        )
        mBinding.tvTime.text = weekDate[0] + "\n月"
        for (i in 1..5) {
            val text = viewModel.daysArray[i - 1] + "\n${weekDate[i]}"
            mTextViews[i - 1].text = text
        }
    }

    override fun onResume() {
        super.onResume()
        for (i in 1..5) {
            viewModel.allCourseList[i - 1].observe(
                viewLifecycleOwner,
                {
                    // initWeekPanel(it, i, viewModel.table, mRvItemLists[i - 1], mRvAdapterLists[i - 1])
                }
            )
        }
    }

    companion object {

        private const val TAG = "ScheduleFragment8888"

        @JvmStatic
        fun newInstance(week: Int) = ScheduleFragment().apply {
            arguments = Bundle().apply {
                putInt("week", week)
            }
        }
    }
}
