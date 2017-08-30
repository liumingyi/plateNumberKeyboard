package org.liumingyi.carnokeyboard.plateinputer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import org.liumingyi.carnokeyboard.plateinputer.utils.KeyboardUtils;

/**
 * 呼出车牌号键盘的输入框
 * Created by liumingyi on 2017/8/14.
 */

public class PlateNumberEditText extends AppCompatEditText implements View.OnTouchListener {

  private static final String TAG = "PlateNumberEditText";
  private static final long ANIMATION_DURATION_KEYBOARD = 500;
  private static final long ANIMATION_DURATION_CONTENT = 200;

  private boolean isKeyboardShow = false;

  private int screenHeight;
  private FragmentManager fragmentManager;
  private KeyboardFragment keyboardFragment;

  /**
   * 键盘之外的布局,必须包括了输入框. (如果输入框会被键盘遮挡,用来实现布局上下移动的效果)
   * 不能
   */
  private ViewGroup contentLayout;
  /**
   * 键盘所在区域
   */
  private ViewGroup keyboardLayout;

  private KeyboardStatusChangedListener callback;/* 键盘显示状态回调 */

  private int contentLayoutOffSetY;
  private boolean isContentMoveUp;

  /**
   * 显示键盘的动画监听
   */
  private final Animator.AnimatorListener showAnimatorListener = new Animator.AnimatorListener() {
    @Override public void onAnimationStart(Animator animator) {
      addFragment();
    }

    @Override public void onAnimationEnd(Animator animator) {
      isKeyboardShow = true;
      startContentLayoutAnimation();
      if (callback != null) {
        callback.onKeyboardShow();
      }
    }

    @Override public void onAnimationCancel(Animator animator) {

    }

    @Override public void onAnimationRepeat(Animator animator) {

    }
  };

  /**
   * 隐藏键盘的动画监听
   */
  private final Animator.AnimatorListener hideAnimatorListener = new Animator.AnimatorListener() {
    @Override public void onAnimationStart(Animator animator) {
      if (isContentMoveUp) {
        Log.d(TAG, "需要复位 : " + contentLayoutOffSetY);
        moveContentLayout(contentLayoutOffSetY, 0);
        isContentMoveUp = false;
      }
    }

    @Override public void onAnimationEnd(Animator animator) {
      isKeyboardShow = false;
      saveSelectedCity();
      hideFragment();
      if (callback != null) {
        callback.onKeyboardHide();
      }
    }

    @Override public void onAnimationCancel(Animator animator) {

    }

    @Override public void onAnimationRepeat(Animator animator) {

    }
  };

  private void saveSelectedCity() {
    if (keyboardFragment != null) {
      keyboardFragment.saveCity();
    }
  }

  /**
   * 键盘的touch事件，消耗事件，防止下传到到下层覆盖区域
   */
  private OnTouchListener keyboardLayoutTouchListener = new OnTouchListener() {
    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
      return true;
    }
  };

  private KeyboardFragment.KeyboardInputListener inputListener =
      new KeyboardFragment.KeyboardInputListener() {
        @Override public void onInput(String value) {
          //Toast.makeText(getContext(), "Value : " + value, Toast.LENGTH_SHORT).show();
          append(value);
        }

        @Override public void delete() {
          deleteLastLetter();
        }

        @Override public void confirm() {
          hideKeyboard();
        }
      };

  /**
   * 删除最后一个字符
   */
  private void deleteLastLetter() {
    Editable text = getText();
    if (text.length() < 1) {
      return;
    }
    text.delete(text.length() - 1, text.length());
    if (text.length() < 1 && keyboardFragment != null) {
      keyboardFragment.showCityKeyboard();
    }
  }

  public PlateNumberEditText(Context context) {
    super(context);
    init(context);
  }

  public PlateNumberEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PlateNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    screenHeight = KeyboardUtils.getScreenHeight((Activity) getContext());
    fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    /* 隐藏默认的软键盘 */
    KeyboardUtils.hideSoftKeyboard(context, this);
    setOnTouchListener(this);
  }

  /**
   * 设置控件
   *
   * @param contentLayout 键盘之外的区域
   * @param keyboardLayout 键盘所在区域
   * @param callback 键盘显示，隐藏状态改变的回调接口
   */
  public void setViews(ViewGroup contentLayout, ViewGroup keyboardLayout,
      KeyboardStatusChangedListener callback) {
    this.contentLayout = contentLayout;
    this.keyboardLayout = keyboardLayout;
    this.callback = callback;
    setContentLayoutClickListener();
    setKeyboardSize();
  }

  /**
   * 设置控件
   *
   * @param contentLayout 键盘之外的区域
   * @param keyboardLayout 键盘所在区域
   */
  public void setViews(ViewGroup contentLayout, ViewGroup keyboardLayout) {
    this.contentLayout = contentLayout;
    this.keyboardLayout = keyboardLayout;
    setContentLayoutClickListener();
    setKeyboardSize();
  }

  /**
   * 设置键盘以外区域的点击事件
   */
  private void setContentLayoutClickListener() {
    this.contentLayout.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        hideKeyboard();
      }
    });
  }

  /**
   * 设置键盘的尺寸
   */
  private void setKeyboardSize() {
    ViewGroup.LayoutParams layoutParams = this.keyboardLayout.getLayoutParams();
    layoutParams.width = KeyboardUtils.getScreenWidth((Activity) getContext());
    this.keyboardLayout.setLayoutParams(layoutParams);
    this.keyboardLayout.setOnTouchListener(keyboardLayoutTouchListener);
  }

  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    if (!isKeyboardShow && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
      showKeyboard();
    }
    return false;
  }

  /**
   * 动画 - 如果EditText会被keyboard挡住，将内容区域向上移动
   */
  private void startContentLayoutAnimation() {
    int[] outLocation = new int[2];
    getLocationOnScreen(outLocation);
    int y = outLocation[1];
    int marginScreenBottom = screenHeight - y - getHeight();
    Log.d(TAG, "edit height : " + getHeight() + " , " + marginScreenBottom);
    if (marginScreenBottom < keyboardLayout.getHeight()) {
      contentLayoutOffSetY = marginScreenBottom - keyboardLayout.getHeight();
      Log.d(TAG, "需要移动 : " + contentLayoutOffSetY);
      moveContentLayout(0, contentLayoutOffSetY);
      isContentMoveUp = true;
    }
  }

  /**
   * 动画 - Y轴移动ContentLayout
   *
   * @param start 其实位置
   * @param end 结束位置
   */
  private void moveContentLayout(int start, int end) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(contentLayout, "translationY", start, end);
    animator.setDuration(ANIMATION_DURATION_CONTENT);
    animator.start();
  }

  /**
   * 动画 - 显示键盘
   */
  public void showKeyboard() {
    /* 此时keyboard还没有测量，不知道高度，所以使用了屏幕高度 :) 肯定够用！:)*/
    ObjectAnimator showAnimator =
        ObjectAnimator.ofFloat(keyboardLayout, "translationY", screenHeight, 0);
    showAnimator.setDuration(ANIMATION_DURATION_KEYBOARD);
    showAnimator.setInterpolator(new DecelerateInterpolator());
    showAnimator.addListener(showAnimatorListener);
    showAnimator.start();
  }

  /**
   * 动画 - 隐藏键盘
   */
  public void hideKeyboard() {
    ObjectAnimator hideAnimator =
        ObjectAnimator.ofFloat(keyboardLayout, "translationY", 0, screenHeight);
    hideAnimator.setDuration(ANIMATION_DURATION_KEYBOARD);
    hideAnimator.setInterpolator(new AccelerateInterpolator());
    hideAnimator.addListener(hideAnimatorListener);
    hideAnimator.start();
  }

  /**
   * 隐藏键盘Fragment
   */
  private void hideFragment() {
    if (keyboardFragment != null) {
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.hide(keyboardFragment);
      transaction.commit();
    }
  }

  /**
   * 添加键盘Fragment
   */
  private void addFragment() {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (keyboardFragment == null) {
      keyboardFragment = KeyboardFragment.newInstance();
      keyboardFragment.setPlateNumberKeyboardListener(inputListener);
      transaction.add(keyboardLayout.getId(), keyboardFragment);
    } else {
      transaction.show(keyboardFragment);
      keyboardFragment.showCityKeyboard();
    }
    transaction.commit();
  }

  @Override
  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect);
    if (focused) {

    } else {
      hideKeyboard();
    }
  }

  public interface KeyboardStatusChangedListener {
    void onKeyboardHide();

    void onKeyboardShow();
  }

  public boolean isKeyboardShow() {
    return isKeyboardShow;
  }
}
