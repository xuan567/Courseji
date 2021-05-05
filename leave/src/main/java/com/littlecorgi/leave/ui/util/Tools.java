package com.littlecorgi.leave.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.littlecorgi.leave.PhotoViewActivity;
import com.littlecorgi.leave.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import java.util.ArrayList;

/**
 * 工具类
 */
public class Tools {

    /**
     * 请求权限
     */
    public static void requestPermissions(final AppCompatActivity activity) {
        AndPermission
                .with(activity)
                .runtime()
                .permission(Permission.CAMERA)
                .onGranted(data -> {
                }) // 权限被允许
                .onDenied(data -> {
                }) // 权限被拒绝
                .start();
    }

    /**
     * 打开图库
     */
    public static void openGallery(AppCompatActivity activity, int maxSize) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(maxSize) // 最大图片选择数量 int
                .minSelectNum(1) // 最小选择数量 int
                .imageSpanCount(3) // 每行显示个数 int
                .imageEngine(GlideEngine.createGlideEngine())
                // .selectionMode()// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                // .isPreviewImage(true)// 是否可预览图片 true or false
                // .isPreviewVideo()// 是否可预览视频 true or false
                // .freeStyleCropEnabled() // 是否可播放音频 true or false
                .isCamera(false) // 是否显示拍照按钮 true or false
                // .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                // .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .isEnableCrop(true) // 是否裁剪 true or false
                // .isCompress(true)// 是否压缩 true or false
                // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                // .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                // .isGif(false)// 是否显示gif图片 true or false
                // .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true) // 裁剪框是否可拖拽 true or false
                // .circleDimmedLayer()// 是否圆形裁剪 true or false
                // .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                // .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                // .isOpenClickSound(false)// 是否开启点击声音 true or false
                // .selectionData()// 是否传入已选图片 List<LocalMedia> list
                // .isPreviewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                // .cutOutQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100) // 小于100kb的图片不压缩
                // .synOrAsy(true)//同步true或异步false 压缩 默认同步
                // .cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                // .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true) // 裁剪是否可放大缩小图片 true or false
                // .videoQuality()// 视频录制质量 0 or 1 int
                // .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                // .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                // .recordVideoSecond()//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST); // 结果回调onActivityResult code
    }

    /**
     * 打开拍照
     */
    public static void takingPictures(AppCompatActivity activity) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    /**
     * 打开图库+拍照按钮
     */
    public static void galleryPictures(AppCompatActivity activity, int maxSize) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(maxSize) // 最大图片选择数量 int
                .minSelectNum(1) // 最小选择数量 int
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3) // 每行显示个数 int
                .isCamera(true) // 是否显示拍照按钮 true or false
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .isEnableCrop(true) // 是否裁剪 true or false
                .isCompress(true) // 是否压缩 true or false
                .minimumCompressSize(100) // 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST); // 结果回调onActivityResult code
    }

    /**
     * 加载圆角图片
     *
     * @param context 上下文
     * @param view    图片控件
     * @param url     网络图片地址
     */
    public static void showGlide(Context context, ImageView view, String url) {
        RequestOptions options =
                new RequestOptions()
                        .error(R.drawable.add_picture)
                        .transform(new GlideRoundTransform(context, 5));
        Glide.with(context).load(url).apply(options).into(view);
    }

    /**
     * 跳转到查看图片界面 上下文、list、点击的位置
     */
    public static void startPhotoViewActivity(Activity context, ArrayList<String> list,
                                              int position) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", list);
        intent.putExtras(bundle);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
