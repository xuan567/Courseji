package com.littlecorgi.my.logic.dao;

import android.content.Context;
import android.widget.Toast;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import java.util.ArrayList;
import java.util.List;

/**
 * PickerView的工具类
 */
public class PickerViewHelp {

    /**
     * 返回数据集合
     */
    public static List<String> getNationalList() {
        List<String> list = new ArrayList<>();
        list.add("汉族");
        list.add("蒙古族");
        list.add("回族");
        list.add("藏族");
        list.add("维吾尔族");
        list.add("苗族");
        list.add("彝族");
        list.add("壮族");
        list.add("布依族");
        list.add("朝鲜族");
        list.add("满族");
        list.add("侗族");
        list.add("瑶族");
        list.add("白族");
        list.add("土家族");
        list.add("哈尼族");
        list.add("哈萨克族");
        list.add("傣族");
        list.add("黎族");
        list.add("傈僳族");
        list.add("佤族");
        list.add("畲族");
        list.add("高山族");
        list.add("拉祜族");
        list.add("水族");
        list.add("纳西族");
        list.add("景颇族");
        list.add("东乡族");
        list.add("柯尔克孜族");
        list.add("土族");
        list.add("达斡尔族");
        list.add("仫佬族");
        list.add("羌族");
        list.add("布朗族");
        list.add("撒拉族");
        list.add("毛南族");
        list.add("仡佬族");
        list.add("锡伯族");
        list.add("阿昌族");
        list.add("普米族");
        list.add("塔吉克族");
        list.add("怒族");
        list.add("乌孜别克族");
        list.add("俄罗斯族");
        list.add("鄂温克族");
        list.add("德昂族");
        list.add("保安族");
        list.add("裕固族");
        list.add("京族");
        list.add("塔塔尔族");
        list.add("独龙族");
        list.add("鄂伦春族");
        list.add("赫哲族");
        list.add("门巴族");
        list.add("珞巴族");
        list.add("基诺族");
        return list;
    }

    /**
     * Dialog形式弹出PickerView
     */
    public static void initOptionsPickerView(Context context) {
        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(
                context,
                (options1, option2, options3, v) -> {
                    // 返回的分别是三个级别的选中位置
                    String str =
                            "options1: "
                                    + options1
                                    + "\noptions2: "
                                    + option2
                                    + "\noptions3: "
                                    + options3;
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                })
                .build();
        // 设置数据
        pvOptions.setPicker(getNationalList());
        pvOptions.show();

        // pvOptions.setTitleText("城市选择")
        //         .setContentTextSize(20)//设置滚轮文字大小
        //         .setDividerColor(Color.LTGRAY)//设置分割线的颜色
        //         .setSelectOptions(0, 1)//默认选中项
        //         .setBgColor(Color.BLACK)
        //         .setTitleBgColor(Color.DKGRAY)
        //         .setTitleColor(Color.LTGRAY)
        //         .setCancelColor(Color.YELLOW)
        //         .setSubmitColor(Color.YELLOW)
        //         .setTextColorCenter(Color.LTGRAY)
        //         .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
        //         .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        //         .setLabels("省", "市", "区")
        //         .setOutSideColor(0x00000000) //设置外部遮罩颜色
        //         .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
        //             @Override
        //             public void onOptionsSelectChanged(
        //             int options1,
        //             int options2,
        //             int options3) {
        //                 String str = "options1: " + options1 + "\noptions2: " + options2
        //                         + "\noptions3: " + options3;
        //                 Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        //             }
        //         })
        //         .build();
        //
        // pvOptions.setSelectOptions(1, 1);
        // pvOptions.setPicker(options1Items);//一级选择器
        // pvOptions.setPicker(options1Items, options2Items);//二级选择器
        // /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }
}
