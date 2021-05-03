package com.littlecorgi.commonlib.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.littlecorgi.commonlib.R;

/**
 * Dialog帮助类
 *
 * @author littlecorgi 2021/5/2
 */
public class DialogUtil {

    /**
     * 显示dialog
     *
     * @param context 上下文
     * @param isAlpha 是否需要透明度
     * @param message 显示加载的内容
     * @return 加载动画的dialog
     */
    public static Dialog writeLoadingDialog(Context context, boolean isAlpha, String message) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isAlpha) {
            WindowManager.LayoutParams lp = progressDialog.getWindow().getAttributes();
            lp.alpha = 0.8f;
            progressDialog.getWindow().setAttributes(lp);
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        LinearLayout layout = v.findViewById(R.id.ll_dialog);
        ProgressBar pbProgressBar = v.findViewById(R.id.pb_progress_bar);
        pbProgressBar.setVisibility(View.VISIBLE);

        TextView tv = v.findViewById(R.id.tv_loading);

        if (message == null || message.equals("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(message);
            //R.color.colorAccent 我是拿的主题控件颜色，可以根据需要给
            tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        progressDialog.setContentView(layout,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        return progressDialog;
    }
}
