package org.liumingyi.carnokeyboard.plateinputer.letterkeyboard;

/**
 * 按钮实体类
 * Created by liumingyi on 2017/8/17.
 */

class PlateKey {

  static final int TYPE_NORMAL = 100;
  static final int TYPE_SWITCH = 101;
  static final int TYPE_DELETE = 102;
  static final int TYPE_CONFIRM = 103;

  private int type = TYPE_NORMAL;
  private String value;

  PlateKey(String value) {
    this.value = value;
  }

  PlateKey(int type, String value) {
    this.type = type;
    this.value = value;
  }

  void setType(int type) {
    this.type = type;
  }

  void setValue(String value) {
    this.value = value;
  }

  String getValue() {
    return value;
  }

  int getType() {
    return type;
  }
}
