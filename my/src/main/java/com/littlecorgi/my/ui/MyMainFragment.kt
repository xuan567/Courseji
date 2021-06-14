package com.littlecorgi.my.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.littlecorgi.commonlib.AppViewModel
import com.littlecorgi.commonlib.util.UserSPConstant
import com.littlecorgi.my.R
import com.littlecorgi.my.databinding.MyFragmentBinding
import com.littlecorgi.my.logic.Result
import com.littlecorgi.my.logic.dao.WindowHelp
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
    private val mViewModel: MyMainViewModel by viewModels()
    private val mAppViewModel: AppViewModel by activityViewModels()

    private lateinit var refreshLayout: RefreshLayout
    private lateinit var mTvName: AppCompatTextView
    private lateinit var mTvProfessional: AppCompatTextView
    private lateinit var mIvAvatar: AppCompatImageView
    private var sp: SharedPreferences? = null

    private var mResponseHasInit = false

    /**
     * 获取StartActivity的返回值
     */
    private var mGetContent = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            mAppViewModel.studentId = mViewModel.getUserIdFromSP()
            mViewModel.updateJpushAlias(requireContext())
            initView()
            initObserve()
            initClick()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.my_fragment, container, false)
        Log.d(TAG, "onCreateView: $mAppViewModel")
        mViewModel.mStudentId = mAppViewModel.studentId
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = requireContext().getSharedPreferences(UserSPConstant.FILE_NAME, Context.MODE_PRIVATE)
        initView()
        initObserve()
        initClick()
    }

    override fun onResume() {
        super.onResume()
        if (mViewModel.mStudentId != -1L && mResponseHasInit) {
            val studentResponse = mViewModel.mStudentResponse
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
        if (mViewModel.mStudentId == -1L) {
            // 没有登录
            mBinding.noLogin.root.visibility = View.VISIBLE
            mBinding.hasLogin.root.visibility = View.GONE
            refreshLayout.setEnableRefresh(false)
        } else {
            mBinding.noLogin.root.visibility = View.GONE
            mBinding.hasLogin.root.visibility = View.VISIBLE
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setRefreshHeader(ClassicsHeader(requireContext()))
            refreshLayout.setOnRefreshListener {
                mViewModel.getUserFromServer(requireContext())
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
            if (mViewModel.mStudentId == -1L) {
                mGetContent.launch(Intent(requireContext(), LoginActivity::class.java))
            } else {
                MessageActivity.startMessageActivity(context, mViewModel.mStudentResponse)
            }
        }
        aboutLayout.setOnClickListener {
            AboutActivity.startAboutActivity(context)
        }
        groupLayout.setOnClickListener {
            GroupActivity.startGroupActivity(context)
        }
    }

    private fun initObserve() {
        if (mViewModel.mStudentId != -1L) {
            // 监听获取到的用户信息，用于展示用户登录信息
            mViewModel.getUser().observe(viewLifecycleOwner) { studentResponse ->
                Log.d(TAG, "initData: 用户信息更新")
                if (studentResponse.data.name?.isEmpty() == true) {
                    mTvName.text = "数据异常，请上报"
                } else {
                    mTvName.text = studentResponse.data.name
                }
                mTvProfessional.text = "计算机学院"

                studentResponse.data.avatar = sp?.getString(UserSPConstant.STUDENT_AVATAR, "") ?: ""
                if (studentResponse.data.avatar == null && studentResponse.data.avatar!!.isEmpty()) {
                    Glide.with(this).load(R.drawable.my).into(mIvAvatar)
                } else {
                    Glide.with(this).load(studentResponse.data.avatar).into(mIvAvatar)
                }

                mResponseHasInit = true
            }
            // 加载数据
            mViewModel.loadUser()

            // 监听登录结果，用于刷新
            mViewModel.getLoginResult().observe(viewLifecycleOwner) {
                if (it is Result.Success<*>) {
                    mViewModel.loadUser()
                    refreshLayout.finishRefresh(true)
                } else {
                    refreshLayout.finishRefresh(false)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MyMainFragment"
    }
}