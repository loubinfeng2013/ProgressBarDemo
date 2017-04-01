package com.loubinfeng.www.progressbardemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by loubinfeng on 2017/4/1.
 */

public class GeneralCircleMaterialProgressBar extends View implements View.OnClickListener{

    //绘制的画笔
    private Paint mPaint;
    //画笔的颜色
    private int mColor = Color.BLACK;
    //全路径
    private Path mFullPath;
    //总长度
    private float mFullLength;
    //需要绘制的路径
    private Path mDrawPath;
    //需要绘制的长度
    private float mDrawLength;
    //用来测量path
    private PathMeasure mMeasure;
    //截取path的起点
    private float mStartD;
    //截取path的终点
    private float mStopD;
    private ValueAnimator mValueAnimator;

    public GeneralCircleMaterialProgressBar(Context context) {
        this(context, null);
    }

    public GeneralCircleMaterialProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralCircleMaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(mColor);
        mFullPath = new Path();
        mDrawPath = new Path();
        setOnClickListener(this);
    }

    /**
     * 设置颜色
     * @param color
     */
    public void setColor(int color){
        mColor = color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            int newWidth = Math.min(widthSize,heightSize);
            setMeasuredDimension(newWidth,newWidth);
        }else{
            int newWidth = dp2Px(getContext(),50);
            setMeasuredDimension(newWidth,newWidth);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int pointX = w / 2;
        int pointY = h / 2;
        int r = w / 2 - 10;
        mFullPath.addCircle(pointX,pointY,r, Path.Direction.CW);
        mMeasure = new PathMeasure(mFullPath,true);
        mFullLength = mMeasure.getLength();
        mDrawLength = mFullLength / 10;
        mStartD = 0;
        mStopD = mStartD + mDrawLength;
        mValueAnimator = ValueAnimator.ofFloat(0,1);
        mValueAnimator.setDuration(10000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float)valueAnimator.getAnimatedValue();
                mStartD = (mStartD +  value * mFullLength)%mFullLength;
                mStopD = (mStopD + value * mFullLength)%mFullLength;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawPath.reset();
        mDrawPath.lineTo(0,0);
        mMeasure.getSegment(mStartD,mStopD,mDrawPath,true);
        canvas.drawPath(mDrawPath,mPaint);
    }


    private int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        mValueAnimator.start();
    }
}
