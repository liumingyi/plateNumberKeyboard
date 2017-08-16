package org.liumingyi.carnokeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import org.liumingyi.carnokeyboard.plateinputer.PlateNumberEditText;

public class MainActivity extends AppCompatActivity {

  private PlateNumberEditText plateNumberEditText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    plateNumberEditText = (PlateNumberEditText) findViewById(R.id.plate_number_edt);
    View contentArea = findViewById(R.id.content_area);
    ViewGroup plateKeyArea = (ViewGroup) findViewById(R.id.plate_number_keyboard);

    contentArea.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        plateNumberEditText.hideKeyboard();
      }
    });

    plateNumberEditText.setViews(contentArea, plateKeyArea,
        new PlateNumberEditText.KeyboardStatusChangedListener() {
          @Override public void onStatusChanged() {

          }
        });

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
