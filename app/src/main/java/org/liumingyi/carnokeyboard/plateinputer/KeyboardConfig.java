package org.liumingyi.carnokeyboard.plateinputer;

import java.util.ArrayList;
import java.util.List;

/**
 * 键盘配置器 , 兼具了按钮位置计算
 * Created by liumingyi on 2017/8/17.
 */

class KeyboardConfig {

  private static final float DEFAULT_PERCENT = 0.4f;

  private float heightPercentOfParent = DEFAULT_PERCENT;

  private int width;
  private int height;

  private List<PlateKey[]> list = new ArrayList<>();

  void setMeasuredDimension(int width, int height) {
    this.width = width;
    this.height = height;
  }

  void addLine(String[] data) {
    if (data == null || data.length == 0) {
      return;
    }

    PlateKey[] keys = new PlateKey[data.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = new PlateKey(data[i]);
    }

    list.add(keys);
  }

  void addLine(PlateKey[] data) {
    if (data == null || data.length == 0) {
      return;
    }
    list.add(data);
  }

  float getHeightPercentOfParent() {
    return heightPercentOfParent;
  }

  void setHeightPercentOfParent(float heightPercentOfParent) {
    this.heightPercentOfParent = heightPercentOfParent;
  }

  int getLineCount() {
    return list.size();
  }

  PlateKey[] getLine(int index) {
    return list.get(index);
  }

  boolean isNothingToDraw() {
    return list.size() == 0;
  }

  PlateKey getKey(float x, float y) {
    if (height == 0 || width == 0 || isNothingToDraw()) {
      return null;
    }
    if (x < 0 || x > width) {
      return null;
    }
    if (y < 0 || y > height) {
      return null;
    }

    int itemHeight = height / list.size();
    int row = (int) Math.ceil(y / itemHeight) - 1;/* 减1 表示从0开始计*/
    PlateKey[] keys = list.get(row);

    if (keys.length == 0) {
      return null;
    }

    int itemWidth = width / keys.length;
    int column = (int) Math.ceil(x / itemWidth) - 1;/* 减1 表示从0开始计*/
    return keys[column];
  }
}
