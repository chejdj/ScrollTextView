package com.chejdj.scrolltextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuyangyang on 2019-07-24
 */
public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "ScrolTextView";
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private int textColor = Color.BLACK;
    private float textSize = 15f;
    private String textStyle = "";
    private String text = "";
    private int viewHeight = 0;
    private int viewWidth = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private int textX = 0;
    private int textY = 0;
    private int textWidth = 0;
    private volatile boolean isTextChange = true;
    private int scollMax = 0;

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollText);
        textColor = arr.getColor(R.styleable.ScrollText_text_color, textColor);
        text = arr.getString(R.styleable.ScrollText_text);
        textSize = arr.getDimension(R.styleable.ScrollText_text_size, textSize);
        textStyle = arr.getString(R.styleable.ScrollText_text_style);

        paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        if (textStyle != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "font/" + textStyle);
            paint.setTypeface(font);
        }
        arr.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScheduleDraw(), 100, 20, TimeUnit.MILLISECONDS);
    }

    public void setText(String newText) {
        if (newText != null) {
            text = newText;
            isTextChange = true;
            textX = 0;
        }
    }


    class ScheduleDraw implements Runnable {

        @Override
        public void run() {
            if (isTextChange) {
                measureVarious();
                isTextChange = false;
            }
            if (Math.abs(textX) > scollMax) {
                textX = 0;
            }
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawText(text, textX--, textY, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void measureVarious() {
        textWidth = (int) paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        textY = viewHeight / 2 + (int) distance;
        scollMax = Math.min(viewWidth, textWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        int fontHeight = (int) Math.ceil(fm.descent - fm.ascent);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, fontHeight);
            viewHeight = fontHeight;
        } else {
            setMeasuredDimension(viewWidth, viewHeight);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        scheduledExecutorService.shutdown();
    }
}
