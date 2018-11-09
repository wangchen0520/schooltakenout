package com.wy.schooltakenout.Tool.OrderView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.wy.schooltakenout.Tool.OrderView.util.Utils;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action2;


/**
 * Author: Owen Chan
 * DATE: 2017-01-05.
 */
public class OrderView extends View {

    //默认状态
    private static final int STATE_NORMAL = 0x00;
    //文字收缩
    private static final int STATE_TEXT_SHRINK = 0x01;
    //按钮展开
    private static final int STATE_BTN_EXPAND = 0x02;
    //按钮收缩
    private static final int STATE_BTN_SHRINK = 0x03;
    //文字展开
    private static final int STATE_TEXT_EXPAND = 0x04;
    //数量变化
    private static final int STATE_TEXT_CHANGE = 0x05;
    //当前状态
    private int mCurrentState = STATE_NORMAL;

    private static final  int ANIMATION_DURATION = 300;

    private int textColor = 0xffffff;
    private String textContent;

    private Paint mPaintBg;
    private Paint mPaintNum;
    private Paint mPaintText;
    private Paint mPaintBigText;

    private int mWidth, mHeight;
    private int  mRadius;
    private float mDegrees;
    private int mChange;
    private int mChangeValue;

    private boolean mIsRun = false;

    private int mNum = 0;

    private List<Animator> mAnimatorList;
    private Action2<Integer, Integer> mCallback;

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    //设置状态
    public void setState(int state, int num) {
        mCurrentState = state;
        mNum = num;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_NORMAL:
            case STATE_TEXT_SHRINK:
            case STATE_TEXT_EXPAND:
                int cx = mWidth - mRadius;  //右侧半圆圆心Y坐标
                int cy = mHeight - mRadius; //右侧半圆圆心Y坐标
                canvas.drawCircle(cx, cy, mRadius, mPaintBg);
                canvas.drawRect(cx - mChange, cy - mRadius, cx, cy + mRadius, mPaintBg);
                canvas.drawCircle(cx - mChange, cy, mRadius, mPaintBg);
                if (mCurrentState == STATE_NORMAL) {
                    mPaintBigText.setTextSize(mPaintBigText.getTextSize() / 2);
                    int textX = mWidth / 2;
                    int textY = mHeight / 2 + Utils.getInstance().getTextHeight(textContent, mPaintBigText) / 2;
                    canvas.drawText(textContent, textX, textY, mPaintBigText);
                    mPaintBigText.setTextSize(mPaintBigText.getTextSize() * 2);
                }
                break;
            case STATE_TEXT_CHANGE:
            case STATE_BTN_EXPAND:
            case STATE_BTN_SHRINK:
                canvas.save();
                int cxx = mWidth - mRadius;
                int cyy = mHeight - mRadius;
                canvas.translate(-mChange, 0);
                if (mCurrentState != STATE_TEXT_CHANGE) {
                    canvas.rotate(mDegrees, cxx, cyy);
                }
                mPaintBg.setStrokeWidth(10);
                mPaintBg.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(cxx, cyy, mRadius - 10, mPaintBg);
                mPaintBg.setStyle(Paint.Style.FILL);
                //绘制减号
                canvas.drawLine(cxx + mRadius / 3, cyy, cxx - mRadius / 3, cyy, mPaintBg);
                mPaintBg.setStrokeWidth(0);
                canvas.restore();
                canvas.save();
                mChange = mChange / 2;
                canvas.translate(-mChange, 0);
                mChange = mChange * 2;
                canvas.drawText(String.valueOf(mNum), cxx, cyy + 15, mPaintNum);
                canvas.restore();
                canvas.drawCircle(cxx, cyy, mRadius, mPaintBg);
                if (mCurrentState == STATE_BTN_EXPAND || mCurrentState == STATE_TEXT_CHANGE) {
                    //绘制加号
                    mPaintText.setStrokeWidth(10);
                    canvas.drawLine(cxx + mRadius / 3, cyy, cxx - mRadius / 3, cyy, mPaintText);
                    canvas.drawLine(cxx, cyy - mRadius / 3, cxx, cyy + mRadius / 3, mPaintText);
                    mPaintText.setStrokeWidth(0);
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentState == STATE_NORMAL && !mIsRun) {
                    mNum = 1;
                    mCurrentState = STATE_TEXT_SHRINK;
                    mIsRun = true;
                    state1();
                } else if ((mCurrentState == STATE_BTN_EXPAND || mCurrentState == STATE_TEXT_CHANGE) && !mIsRun) {
                    PointF eventP = new PointF(event.getX(), event.getY());
                    boolean isLeftInCircle = Utils.getInstance().isPointInCircle(eventP, new PointF(mWidth - mChangeValue - mRadius, mHeight / 2), mRadius);
                    boolean isRightInCircle = Utils.getInstance().isPointInCircle(eventP, new PointF(mWidth - mRadius, mHeight / 2), mRadius);
                    if (isLeftInCircle) {
                        if (mChange != mChangeValue)
                            mChange = mChangeValue;
                        mCurrentState = STATE_TEXT_CHANGE;
                        mNum--;
                        if (mNum == 0) {
                            mCurrentState = STATE_BTN_SHRINK;
                            mIsRun = true;
                            state3();
                            if (mCallback != null) {
                                mCallback.call(mCurrentState, -1);
                            }
                        } else {
                            mCurrentState = STATE_TEXT_CHANGE;
                            invalidate();
                            if (mCallback != null) {
                                mCallback.call(mCurrentState, -1);
                            }
                        }
                    } else if (isRightInCircle) {
                        mCurrentState = STATE_TEXT_CHANGE;
                        if ((mNum + 1) <= Integer.MAX_VALUE) {
                            mNum++;
                            invalidate();
                        } else {
                            Toast.makeText(getContext(), "too much", Toast.LENGTH_LONG).show();
                        }
                        if (mChange != mChangeValue)
                            mChange = mChangeValue;
                        if (mCallback != null) {
                            mCallback.call(mCurrentState, 1);
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimator();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        clear();
    }

    public void clear() {
        if (mAnimatorList != null) {
            for (Animator animator : mAnimatorList)
                animator.cancel();
            mAnimatorList.clear();
        }
        if (mChange != mChangeValue)
            mChange = mChangeValue;
        mDegrees = 360f;
        mIsRun = false;
        if (mCurrentState == STATE_TEXT_SHRINK)
            mCurrentState = STATE_BTN_EXPAND;
        else if (mCurrentState == STATE_BTN_SHRINK || mCurrentState == STATE_TEXT_EXPAND)
            mCurrentState = STATE_NORMAL;
    }


    private void init(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OrderView, 0, 0);
        textColor = attributes.getColor(R.styleable.OrderView_text_color, Color.rgb(255, 255, 255));
        textContent = attributes.getString(R.styleable.OrderView_button_text);
        attributes.recycle();

        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(ResourcesCompat.getColor(getResources(), R.color.subject, getContext().getTheme()));

        mPaintNum = new Paint();
        mPaintNum.setAntiAlias(true);
        mPaintNum.setTextAlign(Paint.Align.CENTER);
        mPaintNum.setTextSize(Utils.getInstance().sp2px(getContext(), 16));
        mPaintNum.setColor(ResourcesCompat.getColor(getResources(), R.color.black, getContext().getTheme()));

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(Utils.getInstance().sp2px(getContext(), 16));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(textColor);

        mPaintBigText = new Paint();
        mPaintBigText.setAntiAlias(true);
        mPaintBigText.setTextSize(Utils.getInstance().sp2px(getContext(), 25));
        mPaintBigText.setTextAlign(Paint.Align.CENTER);
        mPaintBigText.setColor(textColor);

        mWidth = Utils.getInstance().getTextWidth(mPaintBigText, textContent) / 2;
        mHeight = Utils.getInstance().sp2px(getContext(), 15) * 2;
        if (mWidth / (float) mHeight < 3) {
            mWidth = (int) (mHeight * 3);
        }

        mRadius = mHeight / 2;
        mChangeValue = mChange = mWidth - mRadius * 2;
    }


    private void state1() {
        if (mCurrentState == STATE_TEXT_SHRINK) {
            ValueAnimator animator = ValueAnimator.ofInt(mChangeValue, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mIsRun) {
                        mChange = (int) animation.getAnimatedValue();
                        invalidate();
                    } else {
                        animation.cancel();
                    }
                }
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mIsRun = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mIsRun = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCurrentState = STATE_BTN_EXPAND;
                    if (mCallback != null) {
                        mCallback.call(mCurrentState, 1);
                    }
                    state2();
                }
            });
            animator.setDuration(ANIMATION_DURATION);
            animator.start();
            animators().add(animator);
        }
    }

    private void state2() {
        if (mCurrentState == STATE_BTN_EXPAND && mIsRun) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mChangeValue);
            animator.setDuration(ANIMATION_DURATION);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mIsRun) {
                        mChange = (int) animation.getAnimatedValue();
                        mDegrees = mChange / (float) mChangeValue * 360f;
                        invalidate();
                    } else {
                        animation.cancel();
                    }

                }
            });
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mIsRun = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mIsRun = false;
                }
            });
            animator.start();
            animators().add(animator);
        }
    }

    private void state3() {
        if (mCurrentState == STATE_BTN_SHRINK) {
            ValueAnimator animator = ValueAnimator.ofInt(mChangeValue, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mIsRun) {
                        mChange = (int) animation.getAnimatedValue();
                        mDegrees = mChange / (float) mChangeValue * 360f;
                        invalidate();
                    } else {
                        animation.cancel();
                    }
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mIsRun = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mIsRun = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCurrentState = STATE_TEXT_EXPAND;
                    state4();
                }
            });
            animator.setDuration(ANIMATION_DURATION);
            animator.start();
            animators().add(animator);
        }
    }

    private void state4() {
        if (mCurrentState == STATE_TEXT_EXPAND && mIsRun) {
            ValueAnimator animator = ValueAnimator.ofInt(0, mChangeValue);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mIsRun) {
                        mChange = (int) animation.getAnimatedValue();
                        invalidate();
                    } else {
                        animation.cancel();
                    }
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCurrentState = STATE_NORMAL;
                    mIsRun = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mIsRun = false;
                }
            });
            animator.setDuration(ANIMATION_DURATION);
            animator.start();
            animators().add(animator);
        }
    }

    /**
     * 增加数量变化监听
     */
    public void setCallback(Action2<Integer, Integer>  callback) {
        this.mCallback = callback;
    }

    private List<Animator> animators() {
        if (mAnimatorList == null)
            mAnimatorList = new ArrayList<>();
        return mAnimatorList;
    }

    private void clearAnimator() {
        if (mAnimatorList != null) {
            for (Animator animator : mAnimatorList)
                animator.cancel();
            mAnimatorList.clear();
            mAnimatorList = null;
        }
    }

//    public TagView getTagView() {
//        return new TagView(getContext());
//    }

//    public class TagView extends View{
//        int mWidth = mRadius * 2;
//
//        public TagView(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//            //绘制圆
//            canvas.drawCircle(mRadius, mRadius, mRadius, mPaintBg);
//            //绘制加号
//            mPaintText.setStrokeWidth(10);
//            canvas.drawLine(mWidth / 3, mRadius, 2 * mWidth / 3, mRadius, mPaintText);
//            canvas.drawLine(mRadius, mWidth / 3, mRadius, 2 * mWidth / 3, mPaintText);
//            mPaintText.setStrokeWidth(0);
//        }
//
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            setMeasuredDimension(mWidth, mWidth);
//        }
//    }
}
