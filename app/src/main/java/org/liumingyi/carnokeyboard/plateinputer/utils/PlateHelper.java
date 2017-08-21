package org.liumingyi.carnokeyboard.plateinputer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存上一次选择的城市
 * Created by liumingyi on 2017/8/18.
 */

public class PlateHelper {

  private static final String PREF_PLATE_CITY = "pref_plate_city";
  private static final String KEY_PLATE_CITY = "key_plate_city";

  private SharedPreferences preferences;

  private String city;

  public PlateHelper(Context context) {
    preferences = context.getSharedPreferences(PREF_PLATE_CITY, Context.MODE_PRIVATE);
    setCity(preferences.getString(KEY_PLATE_CITY, ""));
  }

  public String getCity() {
    return city;
  }

  private void setCity(String city) {
    this.city = city;
  }

  public void saveCity(String city) {
    save(KEY_PLATE_CITY, city);
  }

  private void save(String key, String value) {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(key, value).apply();
  }
}
