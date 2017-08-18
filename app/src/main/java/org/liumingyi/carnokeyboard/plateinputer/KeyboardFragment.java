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

  private KeyboardInputListener inputListener;

  private PlateCityKeyboard plateKeyboard;
  private PlateLetterKeyboard letterKeyboard;

  private PlateCityKeyboard.PlateKeyboardClickListener plateKeyboardClickListener =
      new PlateCityKeyboard.PlateKeyboardClickListener() {
        @Override public void onClick(String value) {
          if (inputListener != null) {
            inputListener.onInput(value);
          }
        }

        @Override public void goLetterKeyboard() {
          letterKeyboard.setVisibility(View.VISIBLE);
          plateKeyboard.setVisibility(View.GONE);
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

  private PlateLetterKeyboard.PlateKeyboardClickListener letterKeyboardClickListener =
      new PlateLetterKeyboard.PlateKeyboardClickListener() {
        @Override public void onClick(String value) {
          if (inputListener != null) {
            inputListener.onInput(value);
          }
        }

        @Override public void goLetterKeyboard() {
          letterKeyboard.setVisibility(View.GONE);
          plateKeyboard.setVisibility(View.VISIBLE);
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
    plateKeyboard = view.findViewById(R.id.plate_keyboard);
    letterKeyboard = view.findViewById(R.id.plate_letter_keyboard);
    plateKeyboard.setOnPlateKeyboardClickListener(plateKeyboardClickListener);
    letterKeyboard.setOnPlateKeyboardClickListener(letterKeyboardClickListener);
  }

  //private void initLetterKeyboard() {
  //  final String[] line_0 = new String[] {
  //      "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
  //  };
  //  final String[] line_1 = new String[] {
  //      "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
  //  };
  //  final String[] line_2 = new String[] {
  //      "A", "S", "D", "F", "G", "H", "J", "K", "L",
  //  };
  //  final String[] line_3 = new String[] {
  //      "Z", "X", "C", "V", "B", "N", "M",
  //  };
  //  PlateKey[] funcKeys = new PlateKey[3];
  //  funcKeys[0] =
  //      new PlateKey(PlateKey.TYPE_SWITCH, getResources().getString(R.string.simple_city_name));
  //  funcKeys[1] = new PlateKey(PlateKey.TYPE_DELETE, getResources().getString(R.string.delete));
  //  funcKeys[2] = new PlateKey(PlateKey.TYPE_CONFIRM, getResources().getString(R.string.confirm));
  //
  //  KeyboardConfig letterConfig = new KeyboardConfig();
  //  letterConfig.addLine(line_0);
  //  letterConfig.addLine(line_1);
  //  letterConfig.addLine(line_2);
  //  letterConfig.addLine(line_3);
  //  letterConfig.addLine(funcKeys);
  //  letterKeyboard.setConfig(letterConfig);
  //}

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
