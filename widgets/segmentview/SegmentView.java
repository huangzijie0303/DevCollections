package com.zenchn.support.widget.segment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zenchn.support.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:Hzj
 * @date :2019/9/17/017
 * desc  ：ios风格分段控件
 * record：
 */
public class SegmentView extends View {

    private int mMainColor;
    private int mSubColor;
    private int mCornerRadius;
    private int mStrokeWidth;
    private int mHPadding;
    private int mVPadding;

    float[] mRadiiFirst;
    float[] mRadiiLast;

    private int mTouchSlop;
    private boolean inTapRegion;
    private float mStartX;
    private float mStartY;
    private int mCurrentIndex;
    private int mItemWidth;
    private int mItemHeight;

    private List<String> mTitleList;
    private Rect[] mRects;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    GradientDrawable mRound = new GradientDrawable();

    private float mTextSize;


    public SegmentView(Context context) {
        this(context, null);
    }

    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mTouchSlop = touchSlop * touchSlop;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SegmentView);

        mTextSize = a.getDimension(R.styleable.SegmentView_android_textSize, 14 * dm.scaledDensity);

        mMainColor = a.getColor(R.styleable.SegmentView_svMainColor, 0);
        mSubColor = a.getColor(R.styleable.SegmentView_svSubColor, Color.WHITE);
        mCornerRadius = a.getDimensionPixelSize(R.styleable.SegmentView_svCornerRadius, -1);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.SegmentView_svStrokeWidth, (int) (2 * dm.density));

        mHPadding = a.getDimensionPixelSize(R.styleable.SegmentView_svHPadding, (int) (5 * dm.density));
        mVPadding = a.getDimensionPixelSize(R.styleable.SegmentView_svVPadding, (int) (0 * dm.density));

        String svTexts = a.getString(R.styleable.SegmentView_svTexts);
        a.recycle();

        if (!TextUtils.isEmpty(svTexts)) {
            setTitles(svTexts);
            mCurrentIndex = 0;
        }

        mTextPaint.setColor(mSubColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        mPaint.setColor(mMainColor);
        mPaint.setStrokeWidth(mStrokeWidth);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(mCornerRadius);
        gd.setColor(mSubColor);
        gd.setStroke(mStrokeWidth, mMainColor);
        setBackground(gd);

        mRound.setCornerRadius(mCornerRadius);
        mRound.setColor(mMainColor);
        mRound.setStroke(mStrokeWidth, 0);

        mRadiiFirst = new float[]{mCornerRadius, mCornerRadius, 0f, 0f, 0f, 0f, mCornerRadius, mCornerRadius};
        mRadiiLast = new float[]{0f, 0f, mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius, 0f, 0f};

    }


    private OnSegmentSelectedListener mListener;

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public void setOnItemSelectedListener(OnSegmentSelectedListener listener) {
        mListener = listener;
    }

    public void setTitles(String segmentTexts) {
        if (TextUtils.isEmpty(segmentTexts)) {
            return;
        }
        String[] texts = segmentTexts.split("\\|");
        if (texts.length == 0) {
            return;
        }
        List<String> list = new ArrayList<>();
        for (String s : texts) {
            if (TextUtils.isEmpty(s.replaceAll("[ ]*", ""))) {
                continue;
            }
            list.add(s);
        }
        mTitleList = list;
        mRects = new Rect[mTitleList.size()];
        for (int i = 0; i < mRects.length; i++) {
            mRects[i] = new Rect();
        }
        requestLayout();
    }

    Rect mTemp = new Rect();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mTitleList == null || mTitleList.size() == 0) {
            setMeasuredDimension(normalize(widthMeasureSpec, 0), normalize(heightMeasureSpec, 0));
            return;
        }

        final int count = mTitleList.size();

        mItemHeight = 0;
        mItemWidth = 0;

        for (String text : mTitleList) {
            mTextPaint.getTextBounds(text, 0, text.length(), mTemp);

            int w = mTemp.width() + mHPadding * 2;
            int h = mTemp.height() + mVPadding * 2;
            if (mItemWidth < w) {
                mItemWidth = w;
            }
            if (mItemHeight < h) {
                mItemHeight = h;
            }
        }

        int width = normalize(widthMeasureSpec, mItemWidth * count);
        int height = normalize(heightMeasureSpec, mItemHeight);

        mItemWidth = width / count;
        mItemHeight = height;

        int extra = width % count;
        for (int i = 0; i < count; i++) {
            Rect rect = mRects[i];
            rect.set(0, 0, mItemWidth, mItemHeight);
            rect.offsetTo(i * mItemWidth, 0);
        }

        mRects[count - 1].right += extra;

        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTitleList == null || mTitleList.size() == 0) {
            return;
        }
        int count = mTitleList.size();
        for (int i = 0; i < count; i++) {
            Rect rect = mRects[i];
            boolean selected = mCurrentIndex == i;
            if (i > 0) {
                canvas.drawLine(rect.left, 0, rect.left, rect.height(), mPaint);
            }
            if (selected) {
                // first or last
                if (i == 0 || i == count - 1) {
                    mRound.setBounds(rect);
                    // only one
                    if (count == 1) {
                        mRound.setCornerRadius(mCornerRadius);
                    } else {
                        mRound.setCornerRadii(i == 0 ? mRadiiFirst : mRadiiLast);
                    }
                    mRound.draw(canvas);
                } else {
                    canvas.drawRect(rect, mPaint);
                }
            }

            mTextPaint.setColor(selected ? mSubColor : mMainColor);
            canvas.drawText(mTitleList.get(i), rect.exactCenterX(), rect.exactCenterY() - (mTextPaint.ascent() + mTextPaint.descent()) / 2, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                inTapRegion = true;
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - mStartX);
                int dy = (int) (event.getRawY() - mStartY);
                int distance = dx * dx + dy * dy;

                if (distance > mTouchSlop) {
                    inTapRegion = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (inTapRegion) {
                    int[] loc = new int[2];
                    getLocationOnScreen(loc);
                    mCurrentIndex = (int) (mStartX - loc[0]) / mItemWidth;

                    if (mListener != null) {
                        mListener.onSegmentSelected(mCurrentIndex, mTitleList.get(mCurrentIndex));
                    }
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    int normalize(int spec, int value) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);

        switch (mode) {
            case MeasureSpec.AT_MOST:
                return Math.min(size, value);
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
            default:
                return value;
        }
    }

    public interface OnSegmentSelectedListener {

        /**
         * segment选中回调
         *
         * @param index
         * @param text
         */
        void onSegmentSelected(int index, String text);
    }
}
