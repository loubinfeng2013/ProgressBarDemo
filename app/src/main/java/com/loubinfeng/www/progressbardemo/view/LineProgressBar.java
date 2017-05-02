package com.loubinfeng.www.progressbardemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by test on 2017/5/2.
 */

public class LineProgressBar extends View {

    //画笔
    private Paint mPaint;
    //整段路径
    private Path mFullPath;
    //绘制的路径
    private Path mDrawPath;
    //测量路径
    private PathMeasure mPathMeasure;
    //路径长度
    private float mPathLength;
    //开始点
    private float mStartX;
    //结束点
    private float mEndX;
    //开始点的位移动画
    private ValueAnimator mStartAnimator;
    //结束点的位移动画
    private ValueAnimator mEndAnimator;

    public LineProgressBar(Context context) {
        this(context, null);
    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mFullPath = new Path();
        mDrawPath = new Path();
        mStartAnimator = ValueAnimator.ofFloat(0, 1);
        mStartAnimator.setDuration(1000);
        mStartAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mStartAnimator.setInterpolator(new AccelerateInterpolator());
        mEndAnimator = ValueAnimator.ofFloat(0, 1);
        mEndAnimator.setDuration(1000);
        mEndAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mEndAnimator.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint.setStrokeWidth(h);
        mFullPath.lineTo(w, 0);
        mPathMeasure = new PathMeasure(mFullPath, false);
        mPathLength = mPathMeasure.getLength();
        mStartAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mStartX = ((Float) valueAnimator.getAnimatedValue()) * mPathLength;
            }
        });
        mEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mEndX = ((Float) valueAnimator.getAnimatedValue()) * mPathLength;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawPath.reset();
        mDrawPath.lineTo(0,0);
        mPathMeasure.getSegment(mStartX,mEndX,mDrawPath,true);
        canvas.drawPath(mDrawPath,mPaint);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mStartAnimator != null && mEndAnimator != null) {
                        mStartAnimator.start();
                        mEndAnimator.start();
                    }
                }
            },200);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mStartAnimator != null){
            mStartAnimator.cancel();
            mStartAnimator = null;
        }
        if (mEndAnimator != null){
            mEndAnimator.cancel();
            mEndAnimator = null;
        }
    }
}
