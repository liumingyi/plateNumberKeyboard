package org.liumingyi.carnokeyboard.plateinputer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.liumingyi.carnokeyboard.R;

/**
 * Fragment for Plate keyboard
 * fixme fragment 有点多余，考虑去掉
 * Created by liumingyi on 2017/8/14.
 */

public class KeyboardFragment extends Fragment {

  private PlateNumberKeyboard.PlateKeyboardClickListener clickListener =
      new PlateNumberKeyboard.PlateKeyboardClickListener() {
        @Override public void onClick(String value) {
          if (inputListener != null) {
            inputListener.onInput(value);
          }
        }
      };

  private KeyboardInputListener inputListener;

  public static KeyboardFragment newInstance() {
    return new KeyboardFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_keyboard, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    PlateNumberKeyboard plateKeyboard = view.findViewById(R.id.plate_keyboard);
    plateKeyboard.setOnPlateKeyboardClickListener(clickListener);
  }

  void setPlateNumberKeyboardListener(KeyboardInputListener inputListener) {
    this.inputListener = inputListener;
  }

  interface KeyboardInputListener {
    void onInput(String value);
  }
}
