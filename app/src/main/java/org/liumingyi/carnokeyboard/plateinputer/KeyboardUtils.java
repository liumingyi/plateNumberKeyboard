package org.liumingyi.carnokeyboard.plateinputer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class KeyboardUtils {

  /**
   * 隐藏软键盘
   */
  static void hideSoftKeyboard(Context context, EditText item) {
    int SDK_INT = android.os.Build.VERSION.SDK_INT;
    if (SDK_INT <= 10) {
      item.setInputType(InputType.TYPE_NULL);
    } else {
      ((Activity) context).getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      try {
        Class<EditText> cls = EditText.class;
        Method method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
        method.setAccessible(false);
        method.invoke(item, false);
      } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
   */
  static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
   */
  static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 获取屏幕高度
   */
  static int getScreenHeight(Activity activity) {
    DisplayMetrics metrics = new DisplayMetrics();
    Display display = activity.getWindowManager().getDefaultDisplay();
    display.getMetrics(metrics);
    return metrics.heightPixels;
  }

  /**
   * 获取屏幕宽度
   */
  static int getScreenWidth(Activity activity) {
    DisplayMetrics metrics = new DisplayMetrics();
    Display display = activity.getWindowManager().getDefaultDisplay();
    display.getMetrics(metrics);
    return metrics.widthPixels;
  }

  /**
   * 检测Point是否在4个点代表的矩形范围内
   */
  static boolean checkPointContains(PointF point, int left, int top, int right, int bottom) {
    float x = point.x;
    float y = point.y;
    return x >= left && x <= right && y >= top && y <= bottom;
  }
}
