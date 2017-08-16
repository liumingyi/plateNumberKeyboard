package org.liumingyi.carnokeyboard.plateinputer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import org.liumingyi.carnokeyboard.R;

/**
 * 车牌号输入键盘
 * Created by liumingyi on 2017/8/14.
 */

public class PlateNumberKeyboard extends View implements View.OnTouchListener {

  private static final String TAG = "PlateKeyboard";

  private static final int DEFAULT_ROW_COUNT = 7;
  private static final float TEXT_SIZE = 16;//sp

  private final float textSizePx;//px

  private final String[] cities;
  private Paint paint;
  private Paint textPaint;

  private final int rowCount = DEFAULT_ROW_COUNT;/* 每一行几个字*/
  private final int firstTwoRowCount;/* 前两行总数*/

  private int width;
  private int itemHeight;
  private int itemWidth;

  private String startCity;/* 第一个城市简称*/

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
          int index = getItemIndex(x, y);
          String city = cities[index];
          Log.d(TAG, "onSingleTapUp: " + city);
          if (clickListener != null) {
            clickListener.onClick(city);
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
    cities = getResources().getStringArray(R.array.cities);
    textSizePx = KeyboardUtils.dip2px(getContext(), TEXT_SIZE);
    firstTwoRowCount = rowCount * 2 - 3;
  }

  public PlateNumberKeyboard(Context context) {
    super(context);
    init();
  }

  public PlateNumberKeyboard(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public PlateNumberKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(1);
    paint.setColor(Color.BLACK);

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.BLACK);
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setTextSize(textSizePx);

    setOnTouchListener(this);
    detector = new GestureDetector(getContext(), gestureListener);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    Log.d(TAG, "onMeasure: " + width + " , " + height);
    itemHeight = itemWidth = width / rowCount;
    height = itemHeight * 5;
    setMeasuredDimension(width, height);
  }

  /*
   * 绘制效果：
   * --------  ---  ---  ---  ---  ---
   *          | 津 | 翼  | 晋 | 蒙  | 辽 |
   *    京    | ---  ---  ---  ---  ---
   *          | ---  ---  ---  ---  ---
   *          | 津 | 翼  | 晋 | 蒙  | 辽 |
   * --------  ---  ---  ---  ---  ---
   * ---  ---  ---  ---  ---  ---  ---
   *  浙 | 皖  | 闽 | 赣  | 鲁 | 豫  | 鄂 |
   * ---  ---  ---  ---  ---  ---  ---
   * ...
   * ... 总共32个
   * fix me: 将第一个大按钮占用格子数写活
   */
  @Override protected void onDraw(Canvas canvas) {
    drawTopLine(canvas);

    for (int i = 0; i < cities.length; i++) {
      if (i < firstTwoRowCount) {
        drawFirstTwoRowItem(canvas, i);
        continue;
      }
      drawNormalItem(canvas, i);
    }

  }

  /**
   * 绘制 - 顶上的横线
   */
  private void drawTopLine(Canvas canvas) {
    canvas.drawLine(1, 1, width, 1, paint);
  }

  /**
   * 绘制 - 前两排的item
   */
  private void drawFirstTwoRowItem(Canvas canvas, int i) {
    if (i == 0) {
      drawFirstButton(canvas, cities[i]);
      return;
    }
    /* 前两行特殊绘制*/
    int row = (i + 1) / rowCount;
    int column = (i + 1 + row * 2) % rowCount;

    drawButton(canvas, cities[i], row, column);
  }

  /**
   * 绘制 - 后面三排的item
   */
  private void drawNormalItem(Canvas canvas, int i) {
    int row = (i - firstTwoRowCount) / rowCount + 2;
    int column = (i - firstTwoRowCount) % rowCount;

    drawButton(canvas, cities[i], row, column);
  }

  /**
   * 绘制 - 普通按钮
   */
  private void drawButton(Canvas canvas, String city, int row, int column) {
    int left = column * itemWidth;
    int top = row * itemHeight;
    int right = (column + 1) * itemWidth;
    int bottom = (row + 1) * itemHeight;
    canvas.drawRect(left, top, right, bottom, paint);
    canvas.drawText(city, left + itemWidth / 2f, bottom - itemHeight / 2f + textSizePx / 2f,
        textPaint);
  }

  /**
   * 绘制 - 第一个按钮
   */
  private void drawFirstButton(Canvas canvas, String cityName) {
    /* 第一个高宽是普通格子的两倍*/
    canvas.drawRect(0, 0, 2 * itemWidth, 2 * itemHeight, paint);
    canvas.drawText(cityName, itemWidth, itemHeight + textSizePx / 2f, textPaint);
  }

  /* 点击事件处理*/
  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    //Log.d(TAG, "onTouch: " + motionEvent.getX() + " , " + motionEvent.getY());
    /* 点击事件交给手势检测处理*/
    return detector.onTouchEvent(motionEvent);
  }

  /**
   * 根据坐标确定当前点击的位置代表的城市,在城市数组中的位置
   */
  private int getItemIndex(float x, float y) {

    /* 前两排，因为第一个大按钮，所以特殊处理*/
    if (y < itemHeight * 2) {
      int column = (int) Math.ceil(x / itemWidth) - 1;/* 减1 表示从0开始计*/
      if (column < 2) {/* 在前两排，横着数，前两列都是被第一个（大按钮）占用的*/
        return 0;
      }
      int row = (int) Math.ceil(y / itemHeight) - 1;/* 减1 表示从0开始计*/
      return column + rowCount * row - 1 - 2 * row;/* 1表示第一排开头多用的1个格子 2表示是第二排开头被占用的两个普通格子*/
    }

    int column = (int) Math.ceil(x / itemWidth) - 1;
    int row = (int) Math.ceil(y / itemHeight) - 1;
    return column + (row - 2) * rowCount + firstTwoRowCount;/* row - 2 表示去掉前两排，+ firstTwoRowCount 表示叫上前两排真实占用的鸽子数量*/
  }

  private PlateKeyboardClickListener clickListener;

  interface PlateKeyboardClickListener {
    void onClick(String value);
  }

  void setOnPlateKeyboardClickListener(PlateKeyboardClickListener clickListener) {
    this.clickListener = clickListener;
  }
}
