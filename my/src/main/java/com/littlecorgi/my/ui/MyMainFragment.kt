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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.jpush.android.api.JPushInterface
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.littlecorgi.commonlib.AppViewModel
import com.littlecorgi.commonlib.util.UserSPConstant
import com.littlecorgi.my.R
import com.littlecorgi.my.databinding.MyFragmentBinding
import com.littlecorgi.my.logic.LoginDataSource
import com.littlecorgi.my.logic.LoginRepository
import com.littlecorgi.my.logic.Result
import com.littlecorgi.my.logic.UserRetrofitRepository
import com.littlecorgi.my.logic.dao.WindowHelp
import com.littlecorgi.my.logic.model.StudentResponse
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

    private lateinit var mBinding: MyFragmentBinding
    private lateinit var studentResponse: StudentResponse
    private var studentId: Long = 0
    private lateinit var refreshLayout: RefreshLayout
    private lateinit var mTvName: AppCompatTextView
    private lateinit var mTvProfessional: AppCompatTextView
    private lateinit var mIvAvatar: AppCompatImageView
    private var sp: SharedPreferences? = null
    private var mViewModel: AppViewModel? = null

    /**
     * 获取StartActivity的返回值
     */
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
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.my_fragment, container, false)
        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        Log.d(TAG, "onCreateView: $mViewModel")
        studentId = mViewModel!!.studentId
        return mBinding.root
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
            studentResponse.data.avatar = sp?.getString(UserSPConstant.STUDENT_AVATAR, "") ?: ""
            if (studentResponse.data.avatar == null && studentResponse.data.avatar!!.isEmpty()) {
                Glide.with(this).load(R.drawable.my).into(mIvAvatar)
            } else {
                Glide.with(this).load(studentResponse.data.avatar).into(mIvAvatar)
            }
        }
    }

    private fun initView() {
        refreshLayout = mBinding.refreshLayout
        if (studentId == -1L) {
            // 没有登录
            mBinding.noLogin.root.visibility = View.VISIBLE
            mBinding.hasLogin.root.visibility = View.GONE
            refreshLayout.setEnableRefresh(false)
        } else {
            mBinding.noLogin.root.visibility = View.GONE
            mBinding.hasLogin.root.visibility = View.VISIBLE
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
            refreshLayout.setOnRefreshListener { layout: RefreshLayout ->
                Thread {
                    val result: Result<*> = LoginRepository.getInstance(LoginDataSource())
                        .login(
                            requireContext(),
                            studentResponse.data.email,
                            studentResponse.data.password
                        )
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
            mTvName = mBinding.hasLogin.myName
            mTvProfessional = mBinding.hasLogin.myProfessional
            mIvAvatar = mBinding.hasLogin.myPicture
        }
        initBarColor()
    }

    private fun initBarColor() {
        WindowHelp.setWindowStatusBarColor(activity, R.color.blue)
    }

    private fun initClick() {
        val messageLayout = mBinding.myMessage
        val aboutLayout = mBinding.myAbout
        val groupLayout = mBinding.myAddGroup
        messageLayout.setOnClickListener {
            if (studentId == -1L) {
                mGetContent.launch(Intent(requireContext(), LoginActivity::class.java))
            } else {
                MessageActivity.startMessageActivity(context, studentResponse)
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
            studentResponse = UserRetrofitRepository.getStudentFromSP(sp)
            if (studentResponse.data.name?.isEmpty() == true) {
                mTvName.text = "数据异常，请上报"
            } else {
                mTvName.text = studentResponse.data.name
            }
            mTvProfessional.text = "计算机学院"
        }
    }

    companion object {
        private const val TAG = "MyMainFragment"
    }
}