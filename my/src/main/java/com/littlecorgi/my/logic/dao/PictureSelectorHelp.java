package com.littlecorgi.my.logic.dao;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * 图片选择工具类
 *
 * <p>gitHub地址： https://github.com/LuckSiege/PictureSelector/blob/master/README_CN.md#%E5%8D%95%E7%8B%AC%E6%8B%8D%E7%85%A7
 */
public class PictureSelectorHelp {

    public static final int OPEN_CAMERA = 10;
    public static final int OPEN_ALBUM = 20;

    /**
     * 选择图片
     *
     * @param activity Activity
     * @param result   回调onActivityResult Code
     */
    public static void choicePhoto(Activity activity, int result) {
        // 相册选择多张图片
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageEngine(GlideEngine.createGlideEngine())
                .imageFormat(PictureMimeType.PNG)
                .imageSpanCount(3)
                .isCamera(true)
                .isZoomAnim(true)
                .isCompress(true)
                .isPreviewEggs(true)
                .minimumCompressSize(100) // 小于100kb的图片不压缩
                .isEnableCrop(true)
                .forResult(result); // 结果回调onActivityResult code
    }

    /**
     * 选择视频
     *
     * @param activity Activity
     * @param result   回调onActivityResult Code
     */
    public static void choiceVideo(Activity activity, int result) {
        // 相册选择多个视频
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageEngine(GlideEngine.createGlideEngine())
                .imageFormat(PictureMimeType.ofMP4())
                .imageSpanCount(3)
                .isCompress(true)
                .isPreviewEggs(true)
                .isEnableCrop(true) // 每行显示个数 int
                .forResult(result); // 结果回调onActivityResult code
    }

    /**
     * 打开相机
     *
     * @param activity Activity
     * @param result   回调onActivityResult Code
     */
    public static void openCamera(Activity activity, int result) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .imageFormat(PictureMimeType.PNG)
                .withAspectRatio(1, 1) // int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .circleDimmedLayer(false) // 是否圆形裁剪 true or false
                .isEnableCrop(true) // 是否裁剪 true or false
                .isCompress(true) // 是否压缩 true or false
                .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
                .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
                .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true) // 裁剪是否可放大缩小图片 true or false
                .minimumCompressSize(100) // 小于100kb的图片不压缩
                .synOrAsy(true) // 同步true或异步false 压缩 默认同步
                .forResult(result);
    }

    /**
     * 打开相册
     *
     * @param activity Activity
     * @param result   回调onActivityResult Code
     */
    public static void openAlbum(Activity activity, int result) {
        // 相册选择一张图片
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(
                        PictureConfig.SINGLE) // 单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .imageFormat(PictureMimeType.PNG)
                .imageSpanCount(3)
                .isZoomAnim(true)
                .isCompress(true)
                .isPreviewEggs(true)
                .minimumCompressSize(100) // 小于100kb的图片不压缩
                .isEnableCrop(true)
                .withAspectRatio(1, 1) // int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .circleDimmedLayer(false) // 是否圆形裁剪 true or false
                .freeStyleCropEnabled(false) // 裁剪框是否可拖拽
                .showCropFrame(true) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true) // 裁剪是否可放大缩小图片 true or false
                .forResult(result); // 结果回调onActivityResult code
    }

    /**
     * 打开相册
     *
     * @param activity Activity
     * @param maxSize  最大图片选择数量
     */
    public static void openGallery(AppCompatActivity activity, int maxSize) {
        PictureSelector.create(activity)
                .openGallery(
                        // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        PictureMimeType.ofImage()
                )
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
}
