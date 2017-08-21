package org.liumingyi.carnokeyboard.plateinputer;

/**
 * Base keyboard
 * Created by liumingyi on 2017/8/21.
 */

public interface Keyboard {

  void setOnPlateKeyboardClickListener(PlateKeyboardClickListener listener);

  interface PlateKeyboardClickListener {

    void onClick(String value);

    void toggle();

    void delete();

    void confirm();
  }
}
