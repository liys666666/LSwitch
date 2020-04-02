package com.liys.lswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * @Description: 自定义Switch开关样式
 * @Author: liys
 * @CreateDate: 2020/3/31 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/31 15:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LSwitch extends BaseSwitch{

    int offTextX = 3;

    public LSwitch(Context context) {
        this(context, null);
    }

    public LSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {}

    @Override
    protected float getAnimatorValueOff() {
        return getMeasuredWidth()-getMeasuredHeight()/2;
    }

    @Override
    protected float getAnimatorValueOn() {
        return getMeasuredHeight()/2;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), mHeight/2, mHeight/2, paintTrack);
        if(isShowText){ //绘制文字
            int baseline = getBaseline(paintText);
            if(isChecked){
                canvas.drawText(textOff, (mWidth-mHeight)/2-getTextWidth(paintText, textOff)/2+offTextX, baseline, paintText);
            }else{
                canvas.drawText(textOn, mWidth-(mWidth-mHeight)/2-getTextWidth(paintText, textOn)/2-offTextX, baseline, paintText);
            }
        }
        canvas.drawCircle(animatorValue, mHeight/2, mHeight/2, paintThumb);
    }
}
