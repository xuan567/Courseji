package com.littlecorgi.commonlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import java.util.List;

/**
 * AndPermission的工具类
 */
public class AndPermissionHelp {

    /**
     * 通过AndPermission请求权限
     *
     * @param context        上下文
     * @param permissionList 待请求权限列表
     */
    @SuppressLint("WrongConstant")
    public static void andPermission(Context context, String... permissionList) {
        AndPermission.with(context)
                .runtime()
                .permission(permissionList)
                /*
                可以通过实现Rationale接口重写showRationale方法，
                    这个方法是会在如果用户拒绝过该权限，则下次会走showRationale方法，这里面一般是以一个弹窗方式来提示用户。
                    .rationale(this)则是设置监听
                 */
                /*参数是
                   Rationale<List<String>> rationale接口
                   里面有一个方法
                    void showRationale(Context context, T data, RequestExecutor executor);
                */
                // onGranted则是授权成功之后的回调
                .rationale((context1, data, executor) -> {
                    List<String> permissionNames = Permission.transformText(context1, data);
                    String message = "请授权该下的权限" + "\n" + permissionNames;
                    new android.app.AlertDialog.Builder(context1)
                            .setCancelable(false)
                            .setTitle("提示")
                            .setMessage(message)
                            .setPositiveButton("确定", (dialog, which) -> executor.execute())
                            .setNegativeButton("取消", (dialog, which) -> executor.cancel())
                            .show();
                })
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                })
                // onDenied是权限被拒绝时的回调
                .onDenied(data -> {
                    Toast.makeText(context, "请设置相关权限", Toast.LENGTH_SHORT).show();
                    if (AndPermission.hasAlwaysDeniedPermission(context, data)) {
                        // true，弹窗再次向用户索取权限
                        Toast.makeText(context, "请设置相关权限", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }
}
