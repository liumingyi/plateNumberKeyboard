# plateNumberKeyboard
针对车牌号输入的键盘

### 功能：

1. 包含两个键盘：城市简称输入 & 字母数字输入；
2. 城市简称输入后，自动跳转到数字键盘； 
3. 点击输入框，呼出键盘，点击键盘确认键（or 键盘之外区域）收起键盘；
4. 城市简称第一个按钮放大，显示的城市是上一次输入的城市简称。

---

### 虽然可以用，但写的不满意 :(

> 主要不满意的地方：两个键盘控件绘制都比较固定，不够灵活。怎么改还在想:(

---
 
### 实现方式：
 
 1. 在Activity／Fragment 的布局中放置一个 FrameLayout 和 PlateKeyboardEditText (继承自 EditText)。
 	
 	FrameLayout 是键盘的载体，用来做键盘滑入滑出的动画。
 	
 	PlateKeyboardEditText 用来呼出键盘，控制动画。
 
 2. KeyboardFragment 是管理键盘控件的Fragment。

 	包含两个键盘： 城市简称键盘（PlateCityKeyboard） & 数字字母键盘（PlateLetterKeyboard）。
 	
 	>两个键盘的绘制方式不一样，还没想好怎么统一一下。

 3. PlateCityKeyboard ，从资源文件中取所有城市的简称，以固定的行数，列数，固定的样式进行绘制。

 4. PlateLetterKeyboard ，有一个配置类 KeyboardConfig。

 	需要绘制的数据按行设置，每一行根据数据的数量计算列数。
	
	使用「PlateKey」对象代表按钮，有2种类型：
	- 提供内容输入 （eg: 京，津，翼 ，A ，B ，C ...）
	- 提供操作。只有三种：切换键盘，删除，确认。
	
---


	
