package com.littlecorgi.my.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.jpush.android.api.JPushInterface
import com.littlecorgi.commonlib.App
import com.littlecorgi.commonlib.util.UserSPConstant
import com.littlecorgi.my.logic.LoginDataSource
import com.littlecorgi.my.logic.LoginRepository
import com.littlecorgi.my.logic.Result
import com.littlecorgi.my.logic.UserRetrofitRepository
import com.littlecorgi.my.logic.model.StudentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author littlecorgi 2021/6/10
 */
class MyMainViewModel : ViewModel() {

    private val mSp =
        App.mApplicationContext.getSharedPreferences(UserSPConstant.FILE_NAME, Context.MODE_PRIVATE)
    var mStudentId: Long = 0
    lateinit var mStudentResponse: StudentResponse

    // 用户信息
    private val user: MutableLiveData<StudentResponse> by lazy {
        MutableLiveData<StudentResponse>()
    }

    fun getUser(): LiveData<StudentResponse> = user

    /**
     * 从SharedPreferences获取用户信息
     */
    fun loadUser() {
        mStudentResponse = UserRetrofitRepository.getStudentFromSP(mSp)
        user.value = mStudentResponse
    }

    /**
     * 从SharedPreferences获取用户id
     */
    fun getUserIdFromSP() = mSp!!.getLong(UserSPConstant.STUDENT_USER_ID, -1L)

    // 登录结果
    private val loginResult: MutableLiveData<Result<*>> by lazy {
        MutableLiveData<Result<*>>()
    }

    fun getLoginResult(): LiveData<Result<*>> = loginResult

    /**
     * 网络请求从后台获取用户信息
     *
     * @param context 上下文
     */
    fun getUserFromServer(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = LoginRepository.getInstance(LoginDataSource()).login(
                context, mStudentResponse.data.email, mStudentResponse.data.password
            )
            loginResult.postValue(result)
        }
    }

    /**
     * 更新极光推送设备别名
     *
     *
     */
    fun updateJpushAlias(context: Context) {
        JPushInterface.deleteAlias(context, 0)
        // 刚刚发起一个请求，必须过一段时间再发送另一个请求，否则极光推送会报6022错误
        viewModelScope.launch(Dispatchers.IO) {
            delay(5 * 1000)
            JPushInterface.setAlias(context, 10, "学生$mStudentId")
        }
    }
}
