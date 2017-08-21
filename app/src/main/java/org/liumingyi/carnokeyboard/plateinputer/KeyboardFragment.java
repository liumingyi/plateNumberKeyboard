package org.liumingyi.carnokeyboard.plateinputer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.liumingyi.carnokeyboard.R;
import org.liumingyi.carnokeyboard.plateinputer.citykeyboard.PlateCityKeyboard;
import org.liumingyi.carnokeyboard.plateinputer.letterkeyboard.PlateLetterKeyboard;
import org.liumingyi.carnokeyboard.plateinputer.utils.PlateHelper;

/**
 * Fragment for Plate keyboard
 * fixme fragment 有点多余，考虑去掉
 * Created by liumingyi on 2017/8/14.
 */

public class KeyboardFragment extends Fragment {

  private KeyboardInputListener inputListener;

  private PlateCityKeyboard cityKeyboard;
  private PlateLetterKeyboard letterKeyboard;

  private String firstCity;

  private Keyboard.PlateKeyboardClickListener keyboardClickListener =
      new Keyboard.PlateKeyboardClickListener() {
        @Override public void onClick(String value) {
          if (inputListener != null) {
            inputListener.onInput(value);
          }
          markCity(value);
          showLetterKeyboard();
        }

        @Override public void toggle() {
          switchKeyboard();
        }

        @Override public void delete() {
          if (inputListener != null) {
            inputListener.delete();
          }
        }

        @Override public void confirm() {
          if (inputListener != null) {
            inputListener.confirm();
          }
        }
      };

  private void markCity(String value) {
    if (cityKeyboard.getVisibility() == View.VISIBLE) {
      this.firstCity = value;
    }
  }

  public void saveCity() {
    if (TextUtils.isEmpty(firstCity)) {
      return;
    }
    PlateHelper helper = new PlateHelper(getActivity());
    helper.saveCity(firstCity);
  }

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
    cityKeyboard = (PlateCityKeyboard) view.findViewById(R.id.plate_keyboard);
    letterKeyboard = (PlateLetterKeyboard) view.findViewById(R.id.plate_letter_keyboard);
    cityKeyboard.setOnPlateKeyboardClickListener(keyboardClickListener);
    letterKeyboard.setOnPlateKeyboardClickListener(keyboardClickListener);

    PlateHelper helper = new PlateHelper(getActivity());
    String city = helper.getCity();
    if (!TextUtils.isEmpty(city)) {
      cityKeyboard.setFirstCity(city);
    }
  }

  private void switchKeyboard() {
    if (letterKeyboard.getVisibility() == View.VISIBLE) {
      showCityKeyboard();
    } else {
      showLetterKeyboard();
    }
  }

  public void showLetterKeyboard() {
    letterKeyboard.setVisibility(View.VISIBLE);
    cityKeyboard.setVisibility(View.GONE);
  }

  public void showCityKeyboard() {
    cityKeyboard.setVisibility(View.VISIBLE);
    letterKeyboard.setVisibility(View.GONE);
  }

  ////////////////////////////////////////

  void setPlateNumberKeyboardListener(KeyboardInputListener inputListener) {
    this.inputListener = inputListener;
  }

  interface KeyboardInputListener {
    void onInput(String value);

    void delete();

    void confirm();
  }
}
