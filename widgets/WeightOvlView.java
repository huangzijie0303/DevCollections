package cn.firephoenix.h.iotchina.mvp.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.text.DecimalFormat;

import cn.firephoenix.h.iotchina.R;


public class WeightOvlView extends View {
    private Paint ovlPaint, textPaint, pointerPaint, weightPaint;
    private int width, height, radius;
    private int mSection = 201, moveAngle = 0;
    private String[] weightNum;
    private float downX, moveX, angle, newAngle, lastAngle, lastWeight;
    public static final float MAX_VALUE = 200.0f;
    private DecimalFormat df = new DecimalFormat("##");
    private OnWeightListener listener;
    private int oriWeight;

    public WeightOvlView(Context context) {
        super(context);
    }

    public WeightOvlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeightOvlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        ovlPaint = new Paint();
        ovlPaint.setAntiAlias(true);
        ovlPaint.setStrokeWidth(DensityUtil.dp2px(1));
        ovlPaint.setColor(getResources().getColor(R.color.head_text_gray));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.dp2px(10));
        textPaint.setColor(getResources().getColor(R.color.black));

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setStrokeWidth(DensityUtil.dp2px(2));
        pointerPaint.setColor(getResources().getColor(R.color.theme_default));

        weightPaint = new Paint();
        weightPaint.setAntiAlias(true);
        weightPaint.setTextSize(DensityUtil.dp2px(26));
        weightPaint.setColor(getResources().getColor(R.color.black));

        weightNum = new String[21];
        for (int i = 0; i < 21; i++) {
            weightNum[i] = i * 10 + "";
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        radius = width / 2;
        oriWeight = (int) (width * 0.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        if (lastWeight < 30) {
            lastWeight = 30;
        } else if (lastWeight > 200) {
            lastWeight = 200;
        }
        String text = df.format(lastWeight);
        Rect weightRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), weightRect);
        canvas.drawText(text, width / 2 - weightRect.width(), DensityUtil.dp2px(100), weightPaint);
        canvas.drawText("kg", width / 2 + weightRect.width() * 2f, DensityUtil.dp2px(100), textPaint);

        canvas.rotate(90f, width / 2, width / 2);
        canvas.drawLine(DensityUtil.dp2px(20), width / 2, DensityUtil.dp2px(70), width / 2, pointerPaint);
        canvas.drawCircle(DensityUtil.dp2px(75), width / 2, DensityUtil.dp2px(5), pointerPaint);
        canvas.save();
        Rect rect = new Rect();
        textPaint.getTextBounds(weightNum[0], 0, weightNum[0].length(), rect);
        canvas.rotate(newAngle, width / 2, width / 2);
        angle = 360f / mSection;
        for (int i = 0; i < mSection; i++) {
            canvas.rotate(angle, width / 2, width / 2);
            if (i % 10 == 0 && i != 0) {
                canvas.drawLine(DensityUtil.dp2px(20), width / 2, DensityUtil.dp2px(40),
                        width / 2 , ovlPaint);
                canvas.drawText(weightNum[(i / 10)], DensityUtil.dp2px(45), width / 2 + rect.height() / 2, textPaint);
            } else if (i % 5 == 0) {
                canvas.drawLine(DensityUtil.dp2px(20), width / 2, DensityUtil.dp2px(35), width / 2, ovlPaint);
            } else {
                canvas.drawLine(DensityUtil.dp2px(20), width / 2, DensityUtil.dp2px(30), width / 2, ovlPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                float distance = moveX - downX;
                newAngle = lastAngle + (int) (distance * 1.0f / getMeasuredWidth() * 180);
                newAngle++;
                Log.e("TAG", "angle = " + newAngle);
                if (newAngle >= 0) {
                    if (newAngle > 360) {
                        newAngle %= 360;
                    }
                    lastWeight = MAX_VALUE * (1 - newAngle / 360.0f);
                } else {
                    if (Math.abs(newAngle) > 360) {
                        newAngle %= 360;
                    }
                    lastWeight = MAX_VALUE * (Math.abs(newAngle) / 360.0f);
                }
                if (lastWeight < 0) {
                    lastWeight = MAX_VALUE + lastWeight;
                    lastAngle = 0;
                }
                if (lastWeight == MAX_VALUE) {
                    lastAngle = 0;
                    lastWeight = 0;
                }
                invalidate();
                listener.onWeightSet(oriWeight, (int) lastWeight);
                break;
            case MotionEvent.ACTION_UP:
                lastAngle = newAngle;
                downX = 0;
                break;
        }
        return true;
    }

    public void setWeight(float weight) {
        lastWeight = weight;
        newAngle = (1 - weight / MAX_VALUE) * 360;
        invalidate();
        listener.onWeightSet(oriWeight, (int) weight);
    }

    public String getWeight() {
        return df.format(lastWeight);
    }

    public void setOnWeightListener(OnWeightListener listener) {
        this.listener = listener;
    }

    public interface OnWeightListener {
        void onWeightSet(int orginalWeight, int weight);
    }

}
