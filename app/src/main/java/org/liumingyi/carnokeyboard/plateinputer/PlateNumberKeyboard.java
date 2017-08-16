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
  private static final int TOTAL_ROWS = 6;//总行(排)数
  private static final int KEY_GO_LETTER_KEYBOARD = 1000;
  private static final int KEY_DELETE = 1001;
  private static final int KEY_CONFIRM = 1002;

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

          if (index < 0) {
            return false;
          }

          if (index >= 0 && index < cities.length) {
            String city = cities[index];
            Log.d(TAG, "onSingleTapUp: " + city);
            if (clickListener != null) {
              clickListener.onClick(city);
            }
            return false;
          }

          switch (index) {
            case KEY_GO_LETTER_KEYBOARD:
              if (clickListener != null) {
                clickListener.goLetterKeyboard();
              }
              break;
            case KEY_DELETE:
              if (clickListener != null) {
                clickListener.delete();
              }
              break;
            case KEY_CONFIRM:
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
    cities = getResources().getStringArray(R.array.plate_cities);
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
    height = itemHeight * TOTAL_ROWS;
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

    drawActionItems(canvas);
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
   * 绘制 - 操作区域的items{键盘切换，删除，确认}
   */
  private void drawActionItems(Canvas canvas) {
    drawCustomWidthButton(0, 2, 5, getResources().getString(R.string.letter_number_keyboard),
        canvas);
    drawCustomWidthButton(3, 4, 5, getResources().getString(R.string.delete), canvas);
    drawCustomWidthButton(5, 6, 5, getResources().getString(R.string.confirm), canvas);
  }

  /**
   * 绘制 - 第一个按钮
   */
  private void drawFirstButton(Canvas canvas, String cityName) {
    /* 第一个高宽是普通格子的两倍*/
    drawButton(canvas, 0, 0, 2 * itemWidth, 2 * itemHeight, cityName);
  }

  /**
   * 绘制 - 普通按钮
   */
  private void drawButton(Canvas canvas, String city, int row, int column) {
    int left = column * itemWidth;
    int top = row * itemHeight;
    int right = (column + 1) * itemWidth;
    int bottom = (row + 1) * itemHeight;
    drawButton(canvas, left, top, right, bottom, city);
  }

  /**
   * 绘制 - 自定义宽度的按钮
   * eg: (0,2,5,"删除",canvas) 表示：在第6行，占用第0～2列，内容为"删除"的按钮
   *
   * @param startX 起始列
   * @param endX 结束列
   * @param row 所在的行
   * @param value 按钮显示的内容
   */
  private void drawCustomWidthButton(int startX, int endX, int row, String value, Canvas canvas) {
    int left = startX * itemWidth;
    int top = itemHeight * row;
    int right = (endX + 1) * itemWidth;
    int bottom = top + itemHeight;
    drawButton(canvas, left, top, right, bottom, value);
  }

  /**
   * 绘制 - 最终进行绘制的方法
   */
  private void drawButton(Canvas canvas, int left, int top, int right, int bottom, String value) {
    canvas.drawRect(left, top, right, bottom, paint);
    canvas.drawText(value, (left + right) / 2f, (bottom + top) / 2f + textSizePx / 2f, textPaint);
  }

  /* 点击事件处理*/
  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    /* 点击事件交给手势检测处理*/
    return detector.onTouchEvent(motionEvent);
  }

  /**
   * 根据坐标确定当前点击的位置代表的城市,在城市数组中的位置
   */
  private int getItemIndex(float x, float y) {

    int column = (int) Math.ceil(x / itemWidth) - 1;/* 减1 表示从0开始计*/
    int row = (int) Math.ceil(y / itemHeight) - 1;/* 减1 表示从0开始计*/

    /* 前两排，因为第一个大按钮，所以特殊处理*/
    if (row < 2) {
      if (column < 2) {
        return 0;
      }
      return column + rowCount * row - 1 - 2 * row;/* 1表示第一排开头多用的1个格子 2表示是第二排开头被占用的两个普通格子*/
    }

    /*  中间三行普通按钮*/
    if (row >= 2 && row < TOTAL_ROWS - 1) {
      return column + (row - 2) * rowCount + firstTwoRowCount;/* row - 2 表示去掉前两排，+ firstTwoRowCount 表示叫上前两排真实占用的鸽子数量*/
    }

    /* 最后一行的特殊功能按钮 {键盘切换，删除，确认}*/
    if (column > 0 && column < 3) {
      return KEY_GO_LETTER_KEYBOARD;
    }

    if (column >= 3 && column < 5) {
      return KEY_DELETE;
    }

    if (column >= 5 && column < 7) {
      return KEY_CONFIRM;
    }

    return -1;//没有返回-1,异常
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
