package com.liys.lswitch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @Description: 自定义Switch开关--- 基类
 * @Author: liys
 * @CreateDate: 2020/3/31 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/31 15:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseSwitch extends View implements View.OnClickListener {

    protected Context context;

    protected int mWidth = 0;
    protected int mHeight = 0;

    //底部背景
    protected Paint paintTrack = new Paint(); //底部背景
    protected Paint paintThumb = new Paint(); //可滚动部分
    //对应颜色(打开/关闭)
    protected int trackColorOff;
    protected int trackColorOn;
    protected int thumbColorOff;
    protected int thumbColorOn;

    //文字
    protected Paint paintText = new Paint();
    protected String textOff = "";
    protected String textOn = "";
    protected float textSizeOff;
    protected float textSizeOn;
    protected int textColorOff = Color.WHITE; //字体颜色
    protected int textColorOn = Color.WHITE;
    protected boolean isShowText; //是否显示文字
    protected boolean isChecked;
    protected OnCheckedListener onCheckedListener;

    //动画
    protected float animatorValue = 0; //动画变化的值

    public BaseSwitch(Context context) {
        this(context, null);
    }

    public BaseSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttr(attrs);
        initBase();
    }

    protected abstract void init();
    protected abstract float getAnimatorValueOff();
    protected abstract float getAnimatorValueOn();

    protected void unChecked(){}
    protected void checked(){}

    /**
     * 初始化属性
     * @param attrs
     */
    private void initAttr(AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseSwitch);
        //获取自定义属性的值
        //off
        trackColorOff = typedArray.getColor(R.styleable.BaseSwitch_track_color_off, Color.parseColor("#F8933B"));
        thumbColorOff = typedArray.getColor(R.styleable.BaseSwitch_thumb_color_off, Color.WHITE);
        textColorOff = typedArray.getColor(R.styleable.BaseSwitch_text_color_off, Color.WHITE);
        textSizeOff = typedArray.getFloat(R.styleable.BaseSwitch_text_color_off, 10);
        textOff = typedArray.getString(R.styleable.BaseSwitch_text_off);
        if(textOff==null){
            textOff = "开";
        }


        //on
        trackColorOn = typedArray.getColor(R.styleable.BaseSwitch_track_color_on, Color.parseColor("#BECBE4"));
        thumbColorOn = typedArray.getColor(R.styleable.BaseSwitch_thumb_color_on, Color.WHITE);
        textColorOn = typedArray.getColor(R.styleable.BaseSwitch_text_color_on, Color.WHITE);
        textSizeOn = typedArray.getFloat(R.styleable.BaseSwitch_text_color_on, 10);
        textOn = typedArray.getString(R.styleable.BaseSwitch_text_on);
        if(textOn==null){
            textOn = "关";
        }

        //其它属性
        isShowText = typedArray.getBoolean(R.styleable.BaseSwitch_text_show, false);
        isChecked = typedArray.getBoolean(R.styleable.BaseSwitch_checked, true);
    }

    private void initBase() {
        textSizeOff = dp2px(textSizeOff);
        textSizeOn = dp2px(textSizeOn);
        initPaint();
        setOnClickListener(this);
        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getMeasuredWidth();
                mHeight = getMeasuredHeight();
                animatorValue = (isChecked?getAnimatorValueOff():getAnimatorValueOn());
                init();
                invalidate();
            }
        });
    }

    /**
     * 初始化画笔
     */
    private void initPaint(){
        paintTrack.setAntiAlias(true);
        paintThumb.setAntiAlias(true);
        paintText.setAntiAlias(true);
        if(isChecked){
            setPaintOff();
        }else{
            setPaintOn();
        }
    }

    /**
     * 打开
     */
    private void setPaintOff(){
        paintTrack.setColor(trackColorOff);
        paintThumb.setColor(thumbColorOff);
        paintText.setColor(textColorOff);
        paintText.setTextSize(textSizeOff);
    }

    /**
     * 关闭
     */
    private void setPaintOn(){
        paintTrack.setColor(trackColorOn);
        paintThumb.setColor(thumbColorOn);
        paintText.setColor(textColorOn);
        paintText.setTextSize(textSizeOn);
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public void setShowText(boolean showText) {
        isShowText = showText;
    }

    /**
     * 是否选中
     * @param isChecked
     */
    public void setChecked(boolean isChecked){
        if(this.isChecked == isChecked){ //状态一样， 不处理
            return;
        }
        this.isChecked = isChecked;
        if(isChecked){
            setPaintOff();
            startAnimator(getAnimatorValueOn(), getAnimatorValueOff());
            checked();
        }else{
            setPaintOn();
            startAnimator(getAnimatorValueOff(), getAnimatorValueOn());
            unChecked();
        }
        if(onCheckedListener!=null){
            onCheckedListener.onChecked(isChecked);
        }
    }

    protected int getBaseline(Paint paint){
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        // 获取文字的高
        int fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算基线到中心点的距离
        int offY = fontTotalHeight / 2 - fontMetrics.bottom;
        // 计算基线位置
        int baseline = (getMeasuredHeight() + fontTotalHeight) / 2 - offY;
        return baseline;
    }

    @Override
    public void onClick(View v) {
        setChecked(!isChecked);
    }

    /**
     * 动画监听
     * @param startValue 开始的值
     * @param endValue 结束的值
     */
    protected void startAnimator(float startValue, float endValue){
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorValue = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(getAnimatorDuration()); //时间
        animator.start();
    }

    public long getAnimatorDuration(){ //动画时间, 子类可重写
        return 300;
    }

    /**
     * 获取字宽度
     * @param text
     * @return
     */
    protected int getTextWidth(Paint paint, String text){
        if(TextUtils.isEmpty(text)){
            return 0;
        }
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    protected float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    protected float dp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp, getResources().getDisplayMetrics());
    }

    public interface LAnimatorListener {
        void onAnimationUpdate(float value, String type);
    }

    //监听选中情况
    public interface OnCheckedListener{
        void onChecked(boolean isChecked);
    }
}
