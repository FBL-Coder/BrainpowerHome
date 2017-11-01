package cn.etsoft.smarthome.View.RotateBtn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

import static android.content.ContentValues.TAG;

/**
 * 旋转按钮控制
 * Created by yangle on 2016/11/29.
 */
public class RotateControButton extends View {

    // 控件宽
    private int width;
    // 控件高
    private int height;
    // 刻度盘半径
    private int dialRadius;
    // 圆弧半径
    private int arcRadius;
    // 刻度高
    private int scaleHeight = dp2px(10);
    // 刻度盘画笔
    private Paint dialPaint;
    // 圆弧画笔
    private Paint arcPaint;
    // 标题画笔
    private Paint titlePaint;
    // 含义标识画笔
    private Paint valueFlagPaint;
    // 旋转按钮画笔
    private Paint buttonPaint;
    // 值含义显示画笔
    private Paint valuePaint;
    // 文本提示
    private String title_min = "按键命令设置";
    // 当前index
    private int valueNow = 0;
    // 最低值
    private int valueMin = 0;
    // 最高值
    private int valueMax = 4;
    // 四格（每格4.5度，共18度）代表温度1度
    private int angleRate = 4;
    // 按钮图片
    private Bitmap buttonImage = BitmapFactory.decodeResource(getResources(),
            R.drawable.btn_rotate);
    // 按钮图片阴影
    private Bitmap buttonImageShadow = BitmapFactory.decodeResource(getResources(),
            R.drawable.btn_rotate_shadow);
    // 抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    // 刻度值改变监听
    private OnTempChangeListener onTempChangeListener;

    // 以下为旋转按钮相关

    // 当前按钮旋转的角度
    private float rotateAngle;
    // 当前的角度
    private float currentAngle;

    private boolean IsCanTouch = true;

    private List<String> valueString;

    private String Title_Left = "", Title_right = "";

    //屏幕适配系数
    private double Adaptive_coefficient = 1;

    public RotateControButton(Context context) {
        this(context, null);
    }

    public RotateControButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateControButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTitle(String left, String min, String right) {
        Title_Left = left;
        Title_right = right;
        title_min = min;
    }

    public boolean isCanTouch() {
        return IsCanTouch;
    }

    public void setCanTouch(boolean canTouch) {
        IsCanTouch = canTouch;
    }

    private void init() {

        int W = cn.semtec.community2.MyApplication.display_width;
        int h = cn.semtec.community2.MyApplication.display_height;
        int SW = W < h ? W : h;
        int dp_SW = CircleMenuLayout.px2dip(SW);

        if (dp_SW > 200 && dp_SW <= 320) {
            Adaptive_coefficient = 1.8;
        } else if (dp_SW > 320 && dp_SW <= 480) {
            Adaptive_coefficient = 1.8;
        } else if (dp_SW > 480 && dp_SW <= 600) {
            Adaptive_coefficient = 1.5;
        } else if (dp_SW > 600 && dp_SW <= 720) {
            Adaptive_coefficient = 1;
        } else if (dp_SW > 720) {
            Adaptive_coefficient = 0.7;
        }
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setStrokeWidth(dp2px((int)(2/Adaptive_coefficient)));
        dialPaint.setStyle(Paint.Style.STROKE);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.parseColor("#3CB7EA"));
        arcPaint.setStrokeWidth(dp2px((int)(2/Adaptive_coefficient)));
        arcPaint.setStyle(Paint.Style.STROKE);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(sp2px((int)(15/Adaptive_coefficient)));
        titlePaint.setColor(Color.parseColor("#3CB7EA"));
        titlePaint.setStyle(Paint.Style.STROKE);

        valueFlagPaint = new Paint();
        valueFlagPaint.setAntiAlias(true);
        valueFlagPaint.setTextSize(sp2px((int)(18/Adaptive_coefficient)));
        valueFlagPaint.setColor(Color.parseColor("#E4A07E"));
        valueFlagPaint.setStyle(Paint.Style.STROKE);


        buttonPaint = new Paint();
        valueFlagPaint.setAntiAlias(true);
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setTextSize(sp2px((int)(18/Adaptive_coefficient)));
        valuePaint.setColor(Color.parseColor("#E27A3F"));
        valuePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        width = height = Math.min(h, w);
        // 刻度盘半径
        dialRadius = width / 2 - dp2px((int)(15/Adaptive_coefficient));
        // 圆弧半径
        arcRadius = dialRadius - dp2px((int)(15/Adaptive_coefficient));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas);
        drawArc(canvas);
        drawText(canvas);
        drawButton(canvas);
        drawTemp(canvas);
    }

    /**
     * 绘制刻度盘
     *
     * @param canvas 画布
     */
    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 逆时针旋转135-2度
        canvas.rotate(-133);
        dialPaint.setColor(Color.parseColor("#3CB7EA"));
        for (int i = 0; i < 60; i++) {
            canvas.drawLine(0, -dialRadius, 0, -dialRadius + scaleHeight, dialPaint);
            canvas.rotate(4.5f);
        }

        canvas.rotate(90);
        dialPaint.setColor(Color.parseColor("#E37364"));
        for (int i = 0; i < (valueNow - valueMin) * angleRate; i++) {
            canvas.drawLine(0, -dialRadius, 0, -dialRadius + scaleHeight, dialPaint);
            canvas.rotate(4.5f);
        }
        canvas.restore();
    }

    /**
     * 绘制刻度盘下的圆弧
     *
     * @param canvas 画布
     */
    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(135 + 2);
        RectF rectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        canvas.drawArc(rectF, 0, 265, false, arcPaint);
        canvas.restore();
    }

    /**
     * 绘制标题与温度标识
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        canvas.save();

        // 绘制标题
        float titleWidth = titlePaint.measureText(title_min);
        canvas.drawText(title_min, (width - titleWidth) / 2, dialRadius * 2 + dp2px(15), titlePaint);

        // 绘制最小刻度标识
        float tempFlagWidth = titlePaint.measureText(valueMax + "");
        canvas.rotate(55, width / 2, height / 2);
        canvas.drawText(Title_Left, (width - tempFlagWidth) / 2, height + dp2px(5), valueFlagPaint);
        // 绘制最打大刻度标识
        canvas.rotate(-95, width / 2, height / 2);
        canvas.drawText(Title_right, (width - tempFlagWidth) / 2, height + dp2px(5), valueFlagPaint);
        canvas.restore();
    }


    /**
     * 绘制旋转按钮
     *
     * @param canvas 画布
     */
    private void drawButton(Canvas canvas) {
        // 按钮宽高
        int buttonWidth = buttonImage.getWidth();
        int buttonHeight = buttonImage.getHeight();
        // 按钮阴影宽高
        int buttonShadowWidth = buttonImageShadow.getWidth();
        int buttonShadowHeight = buttonImageShadow.getHeight();

        // 绘制按钮阴影
        canvas.drawBitmap(buttonImageShadow, (width - buttonShadowWidth) / 2,
                (height - buttonShadowHeight) / 2, buttonPaint);

        Matrix matrix = new Matrix();
        // 设置按钮位置
        matrix.setTranslate(buttonWidth / 2, buttonHeight / 2);
        // 设置旋转角度
        matrix.preRotate(45 + rotateAngle);
        // 按钮位置还原，此时按钮位置在左上角
        matrix.preTranslate(-buttonWidth / 2, -buttonHeight / 2);
        // 将按钮移到中心位置
        matrix.postTranslate((width - buttonWidth) / 2, (height - buttonHeight) / 2);

        //设置抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawBitmap(buttonImage, matrix, buttonPaint);
    }

    /**
     * 绘制刻度代表含义
     *
     * @param canvas 画布
     */
    private void drawTemp(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);

        float tempWidth = valuePaint.measureText(valueNow + "");
        float tempHeight = (valuePaint.ascent() + valuePaint.descent()) / 2;
        if (valueString == null || valueString.size() == 0 || valueNow >= valueString.size()) {
            Log.i(TAG, "drawTemp: 数据处理异常！");
            return;
        }
        ;
        canvas.drawText(valueString.get(valueNow), tempWidth / 2 - valuePaint.measureText(valueString.get(valueNow)) / 2 - 10, -tempHeight, valuePaint);
        canvas.restore();
    }

    private boolean isDown;
    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!IsCanTouch) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();
                float angle = calcAngle(targetX, targetY);

                // 滑过的角度增量
                float angleIncreased = angle - currentAngle;

                // 防止越界
                if (angleIncreased < -270) {
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > 270) {
                    angleIncreased = angleIncreased - 360;
                }

                IncreaseAngle(angleIncreased);
                currentAngle = angle;
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDown && isMove) {
                    // 纠正指针位置
                    rotateAngle = (float) ((valueNow - valueMin) * angleRate * 4.5);
                    invalidate();
                    // 回调温度改变监听
                    onTempChangeListener.change(valueNow);
                    isDown = false;
                    isMove = false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - width / 2;
        float y = targetY - height / 2;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    /**
     * 增加旋转角度
     *
     * @param angle 增加的角度
     */
    private void IncreaseAngle(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > 270) {
            rotateAngle = 270;
        }
        valueNow = (int) (rotateAngle / 4.5) / angleRate + valueMin;
    }

    /**
     * 设置温度
     *
     * @param valueMin    最小温度
     * @param valueMax    最大温度
     * @param temp        设置的温度
     * @param valueString 值含义集合
     */
    public void setTemp(int valueMin, int valueMax, int temp, List<String> valueString) {
        this.valueMin = valueMin;
        this.valueString = valueString;
        this.valueMax = valueMax;
        this.valueNow = temp;
        this.angleRate = 60 / (valueMax - valueMin);
        rotateAngle = (float) ((temp - valueMin) * angleRate * 4.5);
        invalidate();
    }

    /**
     * 设置温度改变监听
     *
     * @param onTempChangeListener 监听接口
     */
    public void setOnTempChangeListener(OnTempChangeListener onTempChangeListener) {
        this.onTempChangeListener = onTempChangeListener;
    }

    /**
     * 温度改变监听接口
     */
    public interface OnTempChangeListener {
        /**
         * 回调方法
         *
         * @param temp 温度
         */
        void change(int temp);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
