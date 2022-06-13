package com.liys.lswitchs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.liys.lswitch.BaseSwitch;

/**
 * @Description:
 * @Author: liys
 * @CreateDate: 2020/4/5 16:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/5 16:39
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MySwitch extends BaseSwitch{

    public MySwitch(Context context) {
        super(context);
    }

    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void postInit() {

    }


    @Override
    protected float getAnimatorValueStart() {
        return mHeight-mWidth/2;
    }

    @Override
    protected float getAnimatorValueEnd() {
        return mWidth/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), mWidth/2, mWidth/2, paintTrack);
        canvas.drawCircle(mWidth/2, animatorValue, mWidth/2, paintThumb);
    }
}
