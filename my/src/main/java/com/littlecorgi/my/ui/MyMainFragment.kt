package com.littlecorgi.my.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.littlecorgi.commonlib.AppViewModel
import com.littlecorgi.commonlib.util.UserSPConstant
import com.littlecorgi.my.R
import com.littlecorgi.my.logic.LoginDataSource
import com.littlecorgi.my.logic.LoginRepository
import com.littlecorgi.my.logic.Result
import com.littlecorgi.my.logic.UserRetrofitRepository
import com.littlecorgi.my.logic.dao.WindowHelp
import com.littlecorgi.my.logic.model.Student
import com.littlecorgi.my.ui.about.AboutActivity
import com.littlecorgi.my.ui.addgroup.GroupActivity
import com.littlecorgi.my.ui.message.MessageActivity
import com.littlecorgi.my.ui.signin.LoginActivity
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * My 主页
 */
@Route(path = "/my/fragment_my_main")
class MyMainFragment : Fragment() {

    private var mView: View? = null
    private lateinit var student: Student
    private var studentId: Long = 0
    private lateinit var refreshLayout: RefreshLayout
    private lateinit var mTvName: AppCompatTextView
    private lateinit var mTvProfessional: AppCompatTextView
    private lateinit var mIvAvatar: AppCompatImageView
    private var sp: SharedPreferences? = null
    private var mViewModel: AppViewModel? = null

    private var mGetContent = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            studentId = sp!!.getLong(UserSPConstant.STUDENT_USER_ID, -1L)
            mViewModel!!.studentId = studentId
            JPushInterface.deleteAlias(requireContext(), 0)
            // 刚刚发起一个请求，必须过一段时间再发送另一个请求，否则极光推送会报6022错误
            Thread {
                try {
                    Thread.sleep((5 * 1000).toLong())
                    JPushInterface.setAlias(requireContext(), 10, "学生$studentId")
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
            initView()
            initData()
            initClick()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.my_fragment, container, false)
        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        Log.d(TAG, "onCreateView: $mViewModel")
        studentId = mViewModel!!.studentId
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = requireContext().getSharedPreferences(UserSPConstant.FILE_NAME, Context.MODE_PRIVATE)
        initView()
        initData()
        initClick()
    }

    override fun onResume() {
        super.onResume()
        if (studentId != -1L) {
            student.data.avatar = sp!!.getString(UserSPConstant.STUDENT_AVATAR, "")
            if (student.data.avatar.isEmpty()) {
                Glide.with(this).load(R.drawable.my).into(mIvAvatar)
            } else {
                Glide.with(this).load(student.data.avatar).into(mIvAvatar)
            }
        }
    }

    private fun initView() {
        refreshLayout = mView!!.findViewById(R.id.refreshLayout)
        if (studentId == -1L) {
            // 没有登录
            mView!!.findViewById<View>(R.id.no_login).visibility = View.VISIBLE
            mView!!.findViewById<View>(R.id.has_login).visibility = View.GONE
            refreshLayout.setEnableRefresh(false)
        } else {
            mView!!.findViewById<View>(R.id.no_login).visibility = View.GONE
            mView!!.findViewById<View>(R.id.has_login).visibility = View.VISIBLE
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
            refreshLayout.setOnRefreshListener { layout: RefreshLayout ->
                Thread {
                    val result: Result<*> = LoginRepository.getInstance(LoginDataSource())
                        .login(requireContext(), student.data.email, student.data.password)
                    var refreshData = false
                    if (result is Result.Success<*>) {
                        refreshData = true
                    }
                    val finalRefreshData = refreshData
                    // 切换回主线程更新UI （用Java太恶心人了）
                    Handler(Looper.getMainLooper()).post {
                        initData()
                        layout.finishRefresh(finalRefreshData)
                    }
                }.start()
            }
            mTvName = mView!!.findViewById(R.id.my_name)
            mTvProfessional = mView!!.findViewById(R.id.my_professional)
            mIvAvatar = mView!!.findViewById(R.id.my_picture)
        }
        initBarColor()
    }

    private fun initBarColor() {
        WindowHelp.setWindowStatusBarColor(activity, R.color.blue)
    }

    private fun initClick() {
        val messageLayout: ConstraintLayout = mView!!.findViewById(R.id.my_message)
        val aboutLayout: ConstraintLayout = mView!!.findViewById(R.id.my_about)
        val groupLayout: ConstraintLayout = mView!!.findViewById(R.id.my_addGroup)
        messageLayout.setOnClickListener {
            if (studentId == -1L) {
                mGetContent.launch(Intent(requireContext(), LoginActivity::class.java))
            } else {
                MessageActivity.startMessageActivity(context, student)
            }
        }
        aboutLayout.setOnClickListener {
            AboutActivity.startAboutActivity(context)
        }
        groupLayout.setOnClickListener {
            GroupActivity.startGroupActivity(context)
        }
    }

    private fun initData() {
        if (studentId != -1L) {
            student = UserRetrofitRepository.getStudentFromSP(sp)
            if (student.data.name.isEmpty()) {
                mTvName.text = "数据异常，请上报"
            } else {
                mTvName.text = student.data.name
            }
            mTvProfessional.text = "计算机学院"
        }
    }

    companion object {
        private const val TAG = "MyMainFragment"
    }
}