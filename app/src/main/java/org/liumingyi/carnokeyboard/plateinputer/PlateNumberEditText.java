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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * 呼出车牌号键盘的输入框
 * Created by liumingyi on 2017/8/14.
 */

public class PlateNumberEditText extends AppCompatEditText implements View.OnTouchListener {

  private boolean isKeyboardShow = false;

  private int screenHeight;
  private FragmentManager fragmentManager;
  private KeyboardFragment keyboardFragment;
  private View contentArea;/* 输入框和键盘之外的区域 */
  private ViewGroup keyboardArea;/* 键盘所在区域 */
  private KeyboardStatusChangedListener callback;/* 键盘显示状态回调 */

  /**
   * 显示键盘的动画监听
   */
  private final Animator.AnimatorListener showAnimatorListener = new Animator.AnimatorListener() {
    @Override public void onAnimationStart(Animator animator) {
      addFragment();
    }

    @Override public void onAnimationEnd(Animator animator) {

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

    }

    @Override public void onAnimationEnd(Animator animator) {
      isKeyboardShow = false;
      hideFragment();
    }

    @Override public void onAnimationCancel(Animator animator) {

    }

    @Override public void onAnimationRepeat(Animator animator) {

    }
  };

  /**
   * 键盘的touch事件，消耗事件，防止下传到到下层覆盖区域
   */
  private OnTouchListener keyboardAreaTouchListener = new OnTouchListener() {
    @Override public boolean onTouch(View view, MotionEvent motionEvent) {
      return true;
    }
  };

  private KeyboardFragment.KeyboardInputListener inputListener =
      new KeyboardFragment.KeyboardInputListener() {
        @Override public void onInput(String value) {
          Log.d("======", "onClick: " + value);
          //Toast.makeText(getContext(), "Value : " + value, Toast.LENGTH_SHORT).show();
          append(value);
        }
      };

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

  public void setViews(View contentArea, ViewGroup keyboardArea,
      KeyboardStatusChangedListener callback) {
    this.contentArea = contentArea;
    this.keyboardArea = keyboardArea;
    this.callback = callback;
    setKeyboardSize();
  }

  /**
   * 设置键盘的尺寸
   */
  private void setKeyboardSize() {
    ViewGroup.LayoutParams layoutParams = this.keyboardArea.getLayoutParams();
    layoutParams.width = KeyboardUtils.getScreenWidth((Activity) getContext());
    this.keyboardArea.setLayoutParams(layoutParams);
    this.keyboardArea.setOnTouchListener(keyboardAreaTouchListener);
  }

  @Override public boolean onTouch(View view, MotionEvent motionEvent) {
    if (!isKeyboardShow && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
      showKeyboard();
    }
    return false;
  }

  /**
   * 显示键盘
   */
  public void showKeyboard() {
    ObjectAnimator showAnimator =
        ObjectAnimator.ofFloat(keyboardArea, "translationY", screenHeight, 0);
    showAnimator.setDuration(500);
    showAnimator.setInterpolator(new DecelerateInterpolator());
    showAnimator.addListener(showAnimatorListener);
    showAnimator.start();
  }

  /**
   * 隐藏键盘
   */
  public void hideKeyboard() {
    ObjectAnimator hideAnimator =
        ObjectAnimator.ofFloat(keyboardArea, "translationY", 0, screenHeight);
    hideAnimator.setDuration(500);
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
      transaction.add(keyboardArea.getId(), keyboardFragment);
    } else {
      transaction.show(keyboardFragment);
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
    void onStatusChanged();
  }
}
