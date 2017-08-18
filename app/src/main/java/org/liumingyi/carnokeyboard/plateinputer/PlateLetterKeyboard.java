package org.liumingyi.carnokeyboard.plateinputer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import org.liumingyi.carnokeyboard.R;

/**
 * 字母 + 数字 键盘
 * Created by liumingyi on 2017/8/17.
 */

public class PlateLetterKeyboard extends View {

  private static final float TEXT_SIZE = 16;

  private static final String[] line_0 = new String[] {
      "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
  };
  private static final String[] line_1 = new String[] {
      "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
  };
  private static final String[] line_2 = new String[] {
      "A", "S", "D", "F", "G", "H", "J", "K", "L",
  };
  private static final String[] line_3 = new String[] {
      "Z", "X", "C", "V", "B", "N", "M",
  };

  private final float textSizePx;//px

  private KeyboardConfig config;

  private int width;
  private int itemHeight;

  private Paint paint;
  private Paint textPaint;
  private Paint bgPaint;

  private PointF touchPoint;

  private GestureDetector detector;
  private android.view.GestureDetector.OnGestureListener gestureListener =
      new GestureDetector.OnGestureListener() {
        @Override public boolean onDown(MotionEvent motionEvent) {
          return true;
        }

        @Override public void onShowPress(MotionEvent motionEvent) {

        }

        @Override public boolean onSingleTapUp(MotionEvent motionEvent) {
          float x = motionEvent.getX();
          float y = motionEvent.getY();
          PlateKey key = config.getKey(x, y);

          if (key == null) {
            return false;
          }

          switch (key.getType()) {
            case PlateKey.TYPE_NORMAL:
              if (clickListener != null) {
                clickListener.onClick(key.getValue());
              }
              break;
            case PlateKey.TYPE_SWITCH:
              if (clickListener != null) {
                clickListener.goLetterKeyboard();
              }
              break;
            case PlateKey.TYPE_DELETE:
              if (clickListener != null) {
                clickListener.delete();
              }
              break;
            case PlateKey.TYPE_CONFIRM:
              if (clickListener != null) {
                clickListener.confirm();
              }
              break;
          }

          return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v,
            float v1) {
          return false;
        }

        @Override public void onLongPress(MotionEvent motionEvent) {

        }

        @Override public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v,
            float v1) {
          return false;
        }
      };

  {
    PlateKey[] funcKeys = new PlateKey[3];
    funcKeys[0] =
        new PlateKey(PlateKey.TYPE_SWITCH, getResources().getString(R.string.simple_city_name));
    funcKeys[1] = new PlateKey(PlateKey.TYPE_DELETE, getResources().getString(R.string.delete));
    funcKeys[2] = new PlateKey(PlateKey.TYPE_CONFIRM, getResources().getString(R.string.confirm));

    config = new KeyboardConfig();
    config.setHeightPercentOfParent(0.4f);
    config.addLine(line_0);
    config.addLine(line_1);
    config.addLine(line_2);
    config.addLine(line_3);
    config.addLine(funcKeys);

    textSizePx = KeyboardUtils.dip2px(getContext(), TEXT_SIZE);
  }

  public PlateLetterKeyboard(Context context) {
    super(context);
    init();
  }

  public PlateLetterKeyboard(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PlateLetterKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {

    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(1);

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(Color.GREEN);

    detector = new GestureDetector(getContext(), gestureListener);
  }

  @Deprecated public void setConfig(KeyboardConfig config) {
    this.config = config;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    width = MeasureSpec.getSize(widthMeasureSpec);
    int height = (int) (MeasureSpec.getSize(heightMeasureSpec) * 0.4);
    itemHeight = height / config.getLineCount();
    config.setMeasuredDimension(width, height);
    setMeasuredDimension(width, height);
  }

  @Override protected void onDraw(Canvas canvas) {

    if (config == null || config.isNothingToDraw()) {
      return;
    }

    drawTopLine(canvas);

    for (int i = 0, size = config.getLineCount(); i < size; i++) {
      PlateKey[] line = config.getLine(i);
      drawRow(canvas, line, itemHeight * i, itemHeight * (i + 1));
    }
  }

  /**
   * 绘制 - 顶上的横线
   */
  private void drawTopLine(Canvas canvas) {
    canvas.drawLine(1, 1, width, 1, paint);
  }

  /**
   * 绘制 - 行
   */
  private void drawRow(Canvas canvas, PlateKey[] line, int top, int bottom) {
    int count = line.length;
    int itemWidth = width / count;
    for (int i = 0; i < count; i++) {
      drawButton(canvas, itemWidth * i, top, itemWidth * (i + 1), bottom, line[i].getValue());
    }
  }

  /**
   * 绘制 - 按钮
   */
  private void drawButton(Canvas canvas, int left, int top, int right, int bottom, String value) {
    if (touchPoint != null && KeyboardUtils.checkPointContains(touchPoint, left, top, right,
        bottom)) {
      canvas.drawRect(left, top, right, bottom, bgPaint);
    }
    canvas.drawRect(left, top, right, bottom, paint);
    canvas.drawText(value, (left + right) / 2f, (bottom + top) / 2f + textSizePx / 2f, textPaint);
  }

  /* 点击事件处理*/
  @Override public boolean onTouchEvent(MotionEvent event) {
    /* 点击效果*/
    if (MotionEvent.ACTION_DOWN == event.getAction()) {
      showClickEffect(event.getX(), event.getY());
    } else if (MotionEvent.ACTION_UP == event.getAction()) {
      dismissClickEffect();
    }

    /* 点击事件交给手势检测处理*/
    return detector.onTouchEvent(event);
  }

  /**
   * 消除点击效果
   */
  private void dismissClickEffect() {
    if (touchPoint == null) {
      return;
    }
    touchPoint = null;
    invalidate();
  }

  /**
   * 显示点击效果
   */
  private void showClickEffect(float x, float y) {
    if (touchPoint != null) {
      return;
    }
    touchPoint = new PointF(x, y);
    invalidate();
  }

  ///////////////////////监听////////////////////////////

  private PlateKeyboardClickListener clickListener;

  interface PlateKeyboardClickListener {
    void onClick(String value);

    void goLetterKeyboard();

    void delete();

    void confirm();
  }

  void setOnPlateKeyboardClickListener(PlateKeyboardClickListener clickListener) {
    this.clickListener = clickListener;
  }
}
