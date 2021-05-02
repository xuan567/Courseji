package com.littlecorgi.attendance.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.littlecorgi.attendance.R;

/**
 * 标题View
 */
public class TitleView extends RelativeLayout {

    private LinearLayout mLayoutLeft;
    private LinearLayout mLayoutRight;
    private TextView mTvTitle;

    private LeftClickListener mLeftClickListener;
    private RightClickListener mRightClickListener;

    private LinearLayout mRlViewGroup;
    private Button mBtnReturn;
    private Button mBtnReturnText;
    private Button mBtnMenuText;
    private Button mBtnMenu;

    private View mViewLine;

    /**
     * 构造方法
     */
    public TitleView(Context context) {
        super(context, null);
    }

    /**
     * 构造方法
     */
    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_titleview, this);
        initView();
        initAttrs(context, attrs);
        onButtonClick();
    }

    /**
     * 构造方法
     */
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    /**
     * 设置左部布局点击接口
     *
     * @param leftClickListener 左部布局点击接口
     */
    public void setLeftClickListener(LeftClickListener leftClickListener) {
        this.mLeftClickListener = leftClickListener;
    }

    /**
     * 设置右部布局点击接口
     *
     * @param rightClickListener 右部布局点击接口
     */
    public void setRightClickListener(RightClickListener rightClickListener) {
        this.mRightClickListener = rightClickListener;
    }

    /**
     * 设置标题文字
     *
     * @param title 标题文字
     */
    public void setTitleText(String title) {
        mTvTitle.setText(title);
    }

    /**
     * 设置右部菜单字体大小
     *
     * @param size 字体大小
     */
    public void setRightTextSize(float size) {
        if (mBtnMenuText != null) {
            mBtnMenuText.setTextSize(size);
        }
    }

    private void onButtonClick() {
        mLayoutLeft.setOnClickListener(v -> {
            if (mLeftClickListener != null) {
                mLeftClickListener.onLeftButtonClick();
            }
        });

        mLayoutRight.setOnClickListener(v -> {
            if (mRightClickListener != null) {
                mRightClickListener.onRightButtonClick();
            }
        });
    }

    private void initView() {
        mRlViewGroup = findViewById(R.id.rl_viewGroup);
        mLayoutLeft = findViewById(R.id.ll_left);
        mLayoutRight = findViewById(R.id.ll_right);
        mBtnMenuText = findViewById(R.id.tv_menu);
        mBtnReturnText = findViewById(R.id.tv_return);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnMenu = findViewById(R.id.btn_menu);
        mBtnReturn = findViewById(R.id.btn_return);
        mViewLine = findViewById(R.id.view_up_line);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);

        Drawable viewGroupDrawable = typedArray.getDrawable(R.styleable.TitleView_titleBackground);
        if (viewGroupDrawable == null) {
            viewGroupDrawable = ContextCompat.getDrawable(context, R.drawable.shape_gradient_title);
        }
        mRlViewGroup.setBackground(viewGroupDrawable);

        float titleSize = typedArray.getDimension(R.styleable.TitleView_titleTextSize, 22);
        int titleColor = typedArray.getColor(R.styleable.TitleView_titleColor, Color.WHITE);
        String titleText = typedArray.getString(R.styleable.TitleView_titleText);
        mTvTitle.setText(titleText);
        mTvTitle.setTextSize(titleSize);
        mTvTitle.setTextColor(titleColor);

        Drawable leftIcon = typedArray.getDrawable(R.styleable.TitleView_leftIcon);
        if (leftIcon == null) {
            leftIcon = ContextCompat.getDrawable(context, R.mipmap.title_return);
        }
        String leftText = typedArray.getString(R.styleable.TitleView_leftText);
        if (leftText == null || leftText.equals("")) {
            leftText = "返回";
        }
        final float leftTextSize = typedArray.getDimension(R.styleable.TitleView_leftTextSize, 20);
        final int leftTextColor =
                typedArray.getColor(R.styleable.TitleView_titleColor, Color.WHITE);
        boolean leftIconVisibility =
                typedArray.getBoolean(R.styleable.TitleView_leftIconVisibility, true);
        boolean leftTextVisibility =
                typedArray.getBoolean(R.styleable.TitleView_leftTextVisibility, true);
        mBtnReturn.setBackground(leftIcon);
        if (leftIconVisibility) {
            mBtnReturn.setVisibility(View.VISIBLE);
        } else {
            mBtnReturn.setVisibility(View.GONE);
        }
        if (leftTextVisibility) {
            mBtnReturnText.setVisibility(View.VISIBLE);
        } else {
            mBtnReturnText.setVisibility(View.GONE);
        }
        mBtnReturnText.setText(leftText);
        mBtnReturnText.setTextColor(leftTextColor);
        mBtnReturnText.setTextSize(leftTextSize);

        Drawable rightIcon = typedArray.getDrawable(R.styleable.TitleView_rightIcon);
        if (rightIcon == null) {
            rightIcon = ContextCompat.getDrawable(context, R.mipmap.title_menu);
        }
        String rightText = typedArray.getString(R.styleable.TitleView_rightText);
        if (rightText == null || leftText.equals("")) {
            rightText = "菜单";
        }
        final float rightTextSize =
                typedArray.getDimension(R.styleable.TitleView_rightTextSize, 20);
        final int rightTextColor =
                typedArray.getColor(R.styleable.TitleView_rightTextColor, Color.WHITE);
        boolean rightIconVisibility =
                typedArray.getBoolean(R.styleable.TitleView_rightIconVisibility, false);
        boolean rightTextVisibility =
                typedArray.getBoolean(R.styleable.TitleView_rightTextVisibility, false);
        mBtnMenu.setBackground(rightIcon);
        if (rightIconVisibility) {
            mBtnMenu.setVisibility(View.VISIBLE);
        } else {
            mBtnMenu.setVisibility(View.GONE);
        }
        if (rightTextVisibility) {
            mBtnMenuText.setVisibility(View.VISIBLE);
        } else {
            mBtnMenuText.setVisibility(View.GONE);
        }
        mBtnMenuText.setText(rightText);
        mBtnMenuText.setTextColor(rightTextColor);
        mBtnMenuText.setTextSize(rightTextSize);
        typedArray.recycle();
    }

    /**
     * 设置右部菜单View的显示
     *
     * @param visible 显示方式
     */
    public void setRightMenuTextVisible(boolean visible) {
        if (visible) {
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右部文字的显示
     *
     * @param visible 显示方式
     */
    public void setRightTextVisible(boolean visible) {
        if (visible) {
            mBtnMenuText.setVisibility(View.VISIBLE);
        } else {
            mBtnMenuText.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左部菜单View的显示
     *
     * @param visible 显示方式
     */
    public void setLeftMenuTextVisible(boolean visible) {
        if (visible) {
            mLayoutLeft.setVisibility(View.VISIBLE);
        } else {
            mLayoutLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置分割线View的显示
     *
     * @param b 是否显示
     */
    public void setViewUpLineVisible(boolean b) {
        if (b) {
            mViewLine.setVisibility(VISIBLE);
        } else {
            mViewLine.setVisibility(GONE);
        }
    }

    /**
     * 过去右部布局
     */
    public LinearLayout getLayoutRight() {
        return mLayoutRight;
    }

    /**
     * 设置右部文字
     *
     * @param text 需要显示的文字
     */
    public void setRightText(String text) {
        mBtnMenuText.setText(text);
    }

    /**
     * 左部点击接口
     */
    public interface LeftClickListener {

        void onLeftButtonClick();
    }

    /**
     * 右部点击接口
     */
    public interface RightClickListener {

        void onRightButtonClick();
    }
}
