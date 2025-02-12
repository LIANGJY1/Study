//package com.example.study.customUI;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.LinearGradient;
//import android.graphics.Outline;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.RectF;
//import android.graphics.Shader;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewOutlineProvider;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.AppCompatTextView;
//
//import com.hynex.appstore.R;
//import com.hynex.appstore.util.tools.DrawableUtil;
//import com.reachauto.rms.log.RLog;
//
//public class GradientBackgroundView extends AppCompatTextView {
//
//    private Paint mBackgroundPaint;
//    private int mBackgroundColor;
//
//    private int mFillStartColor;
//    private int mFillMidColor;
//    private int mFillEndColor;
//    private int mStrokeStartColor;
//    private int mStrokeMidColor;
//    private int mStrokeEndColor;
//    private int mStrokeWidth;
//    private RectF mStrokeBackgroundBounds;
//    private RectF mFillBackgroundBounds;
//    private float mButtonRadius;
//    private boolean isPressed = false;
//    private boolean isFirstActionDown = false;
//
//    public GradientBackgroundView(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public GradientBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public GradientBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initAttrs(context, attrs);
//        init();
//    }
//
//    private void init() {
//        mBackgroundPaint = new Paint();
//        setOutlineProvider(new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20);
//            }
//        });
//        setClipToOutline(true);
//        invalidate();
//    }
//
//    private void initAttrs(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientBackgroundView);
//        try {
//            mFillStartColor = typedArray.getColor(R.styleable.GradientBackgroundView_fill_start_color, DrawableUtil.getContextColor(R.color.color_fill_start));
//            mFillMidColor = typedArray.getColor(R.styleable.GradientBackgroundView_fill_start_color, DrawableUtil.getContextColor(R.color.color_fill_mid));
//            mFillEndColor = typedArray.getColor(R.styleable.GradientBackgroundView_fill_end_color, DrawableUtil.getContextColor(R.color.color_fill_end));
//            mStrokeStartColor = typedArray.getColor(R.styleable.GradientBackgroundView_stroke_start_color, DrawableUtil.getContextColor(R.color.color_stroke_start));
//            mStrokeMidColor = typedArray.getColor(R.styleable.GradientBackgroundView_stroke_start_color, DrawableUtil.getContextColor(R.color.color_stroke_mid));
//            mStrokeEndColor = typedArray.getColor(R.styleable.GradientBackgroundView_stroke_end_color, DrawableUtil.getContextColor(R.color.color_stroke_end));
//            mStrokeWidth = typedArray.getInt(R.styleable.GradientBackgroundView_stroke_width, 1);
//            mButtonRadius = typedArray.getDimension(R.styleable.GradientBackgroundView_bg_radius, 20);
//        } finally {
//            typedArray.recycle();
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawBackground(canvas, isPressed);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!isClickable()) {
//            return true;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isFirstActionDown = true;
//            case MotionEvent.ACTION_MOVE:
//                if (isFirstActionDown) {
//                    if (event.getX() < 0 || event.getX() > getMeasuredWidth() || event.getY() > getMeasuredHeight() || event.getY() < 0) {
//                        isPressed = false;
//                        isFirstActionDown = false;
//                    } else {
//                        isPressed = true;
//                    }
//
//                }
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                isPressed = false;
//                this.performClick();
//                invalidate();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_OUTSIDE:
//                isPressed = false;
//                invalidate();
//                break;
//        }
//        return true;
//    }
//
//    private void drawBackground(Canvas canvas, boolean isPressed) {
//        if (isPressed) {
//            mStrokeStartColor = DrawableUtil.getContextColor(R.color.color_stroke_start_pressed);
//            mStrokeEndColor = DrawableUtil.getContextColor(R.color.color_stroke_end_pressed);
//            mFillStartColor = DrawableUtil.getContextColor(R.color.color_fill_start_pressed);
//            mFillEndColor = DrawableUtil.getContextColor(R.color.color_fill_end_pressed);
//        } else {
//            mStrokeStartColor = DrawableUtil.getContextColor(R.color.color_stroke_start);
//            mStrokeEndColor = DrawableUtil.getContextColor(R.color.color_stroke_end);
//            mFillStartColor = DrawableUtil.getContextColor(R.color.color_fill_start);
//            mFillEndColor = DrawableUtil.getContextColor(R.color.color_fill_end);
//        }
//        mStrokeBackgroundBounds = new RectF();
//        mStrokeBackgroundBounds.left = 0;
//        mStrokeBackgroundBounds.top = 0;
//        mStrokeBackgroundBounds.right = getMeasuredWidth();
//        mStrokeBackgroundBounds.bottom = getMeasuredHeight();
//        mFillBackgroundBounds = new RectF();
//        mFillBackgroundBounds.left = 1;
//        mFillBackgroundBounds.top = 1;
//        mFillBackgroundBounds.right = getMeasuredWidth() - 1;
//        mFillBackgroundBounds.bottom = getMeasuredHeight() - 1;
//        RLog.i("mBackgroundBounds.right: " + mStrokeBackgroundBounds.right);
//
//        mBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//        mBackgroundPaint.setAntiAlias(true);
//        mBackgroundPaint.setStyle(Paint.Style.STROKE);
//        mBackgroundPaint.setStrokeWidth(1);
//        LinearGradient strokeGradient = new LinearGradient(0, 0, mStrokeBackgroundBounds.right, 0, mStrokeStartColor, mStrokeEndColor,  Shader.TileMode.CLAMP);
//        mBackgroundPaint.setShader(strokeGradient); // 设置Paint的着色器
//        canvas.drawRoundRect(mStrokeBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
//
//        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        LinearGradient fillGradient = new LinearGradient(0, 0, mFillBackgroundBounds.right, 0, mFillStartColor, mFillEndColor, Shader.TileMode.CLAMP);
//        mBackgroundPaint.setShader(fillGradient); // 设置Paint的着色器
//        canvas.drawRoundRect(mFillBackgroundBounds, mButtonRadius, mButtonRadius, mBackgroundPaint);
//    }
//
//}