package com.littlecorgi.courseji.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushMessage;
import es.dmoral.toasty.Toasty;

/**
 * 处理tagalias相关的逻辑。极光推送Demo中的代码
 *
 * @author littlecorgi 2021/05/07
 */
@SuppressLint("CheckResult")
public class TagAliasOperatorHelper {
    private static final String TAG = "JIGUANG-TagAliasHelper";

    private Context mContext;

    private static TagAliasOperatorHelper mInstance;

    private TagAliasOperatorHelper() {
    }

    /**
     * 单例模式获取实例
     *
     * @return 单例对象
     */
    public static TagAliasOperatorHelper getInstance() {
        if (mInstance == null) {
            synchronized (TagAliasOperatorHelper.class) {
                if (mInstance == null) {
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
        }
    }

    /**
     * TAG回调
     *
     * @param context     上下文
     * @param pushMessage 消息
     */
    public void onTagOperatorResult(Context context, JPushMessage pushMessage) {
        int sequence = pushMessage.getSequence();
        Log.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:"
                + pushMessage.getTags());
        Log.i(TAG, "tags size:" + pushMessage.getTags().size());
        init(context);

        if (pushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify tag Success,sequence:" + sequence);
            Toasty.success(context, "modify success");
        } else {
            String logs = "Failed to modify tags";
            if (pushMessage.getErrorCode() == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + pushMessage.getErrorCode();
            Log.e(TAG, logs);
            Toasty.error(context, logs);
        }
    }

    /**
     * 点击TAG的回调
     *
     * @param context     上下文
     * @param pushMessage 消息
     */
    public void onCheckTagOperatorResult(Context context, JPushMessage pushMessage) {
        int sequence = pushMessage.getSequence();
        Log.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:"
                + pushMessage.getCheckTag());
        init(context);
        if (pushMessage.getErrorCode() == 0) {
            String logs =
                    "modify tag " + pushMessage.getCheckTag() + " bind state success,state:"
                            + pushMessage.getTagCheckStateResult();
            Log.i(TAG, logs);
            Toasty.success(context, "modify success");
        } else {
            String logs = "Failed to modify tags, errorCode:" + pushMessage.getErrorCode();
            Log.e(TAG, logs);
            Toasty.error(context, logs);
        }
    }

    /**
     * 别名的回调
     *
     * @param context     上下文
     * @param pushMessage 消息
     */
    public void onAliasOperatorResult(Context context, JPushMessage pushMessage) {
        int sequence = pushMessage.getSequence();
        Log.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:"
                + pushMessage.getAlias());
        init(context);

        if (pushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify alias Success,sequence:" + sequence);
            Toasty.success(context, "modify success");
        } else {
            String logs = "Failed to modify alias, errorCode:" + pushMessage.getErrorCode();
            Log.e(TAG, logs);
            Toasty.error(context, logs);
            // MMKV.defaultMMKV().putString(AdvActivity.ALIAS_DATA, "");
        }
    }

    /**
     * 设置手机号码回调
     *
     * @param context     上下文
     * @param pushMessage 消息
     */
    public void onMobileNumberOperatorResult(Context context, JPushMessage pushMessage) {
        int sequence = pushMessage.getSequence();
        Log.i(TAG,
                "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:"
                        + pushMessage.getMobileNumber());
        init(context);
        if (pushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - set mobile number Success,sequence:" + sequence);
            Toasty.success(context, "modify success");
        } else {
            String logs = "Failed to set mobile number, errorCode:" + pushMessage.getErrorCode();
            Log.e(TAG, logs);
            Toasty.error(context, logs);
            // MMKV.defaultMMKV().putString(AdvActivity.MN_DATA, "");
        }
    }
}
