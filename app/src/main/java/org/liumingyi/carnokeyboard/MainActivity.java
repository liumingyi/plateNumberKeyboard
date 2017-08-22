package org.liumingyi.carnokeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import org.liumingyi.carnokeyboard.plateinputer.PlateNumberEditText;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PlateNumberEditText plateNumberEditText =
        (PlateNumberEditText) findViewById(R.id.plate_number_edt);
    ViewGroup contentArea = (ViewGroup) findViewById(R.id.content_area);
    ViewGroup plateKeyArea = (ViewGroup) findViewById(R.id.plate_number_keyboard);

    plateNumberEditText.setViews(contentArea, plateKeyArea);
    //plateNumberEditText.setViews(contentArea, plateKeyArea,
    //    new PlateNumberEditText.KeyboardStatusChangedListener() {
    //      @Override public void onKeyboardHide() {
    //        Toast.makeText(MainActivity.this, "键盘收起", Toast.LENGTH_SHORT).show();
    //      }
    //
    //      @Override public void onKeyboardShow() {
    //        Toast.makeText(MainActivity.this, "键盘弹出", Toast.LENGTH_SHORT).show();
    //      }
    //    });

    plateNumberEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override public void afterTextChanged(Editable editable) {

      }
    });
  }
}
