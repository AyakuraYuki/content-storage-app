package cc.ayakurayuki.csa.starter.core.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author ayakurayuki
 * @date 2019/05/06-17:04
 */
public class NumberUtils {

  /**
   * 判断两个数是否相等
   *
   * @param num1 数1
   * @param num2 数2
   *
   * @return boolean
   */
  public static boolean equals(long num1, int num2) {
    return num1 == num2;
  }

  /**
   * 判断Number对象是否为空
   *
   * @param num Number对象
   *
   * @return boolean
   */
  public static boolean isNull(Number num) {
    return num == null;
  }

  /**
   * 判断Number对象是否非空
   *
   * @param num Number对象
   *
   * @return boolean
   */
  public static boolean isNotNull(Number num) {
    return num != null;
  }

  /**
   * 返回整数值，为null返回默认整数值
   *
   * @param num        整型对象
   * @param defaultNum 默认整数值
   *
   * @return 整数值
   */
  public static int defaultInt(Integer num, int defaultNum) {
    if (num == null) {
      return defaultNum;
    }
    return num;
  }

  public static double defaultDouble(Double num, double defaultNum) {
    if (num == null) {
      return defaultNum;
    }
    return num;
  }

  public static long defaultLong(Long num, long defaultNum) {
    if (num == null) {
      return defaultNum;
    }
    return num;
  }

  /**
   * 获取百分比.
   *
   * @param current 分子
   * @param total   分母
   *
   * @return 百分比
   */
  public static int percent(double current, double total) {
    double avg = current / total;
    return (int) (avg * 100);
  }

  /**
   * 获取百分比的值
   *
   * @param number 分子
   * @param total  分母
   * @param digits 保留的小数位
   *
   * @return 12.44
   */
  public static float percentAndKeepDecimal(double number, double total, int digits) {
    double val = number / total;
    return (float) (Math.round(val * Math.pow(10, digits) * 100) / Math.pow(10, digits));
  }

  /**
   * 每秒平均值.
   *
   * @param count 总数
   * @param time  秒数
   *
   * @return 平均值
   */
  public static long perSecondAvg(long count, long time) {
    if (time <= 0) {
      return 0;
    }
    return count * 1000 / time;
  }

  /**
   * 把null转换为0.
   *
   * @param num 整型对象
   *
   * @return 整数
   */
  public static int toInt(Number num) {
    if (num == null) {
      return 0;
    } else {
      return num.intValue();
    }
  }

  /**
   * 把null和非正数转成false
   *
   * @param num 长整型
   *
   * @return boolean
   */
  public static boolean toBool(Long num) {
    return num != null && num > 0;
  }

  /**
   * 返回整数值，为null返回默认整数值
   *
   * @param num          整型对象
   * @param defaultValue 默认整数值
   *
   * @return 整数值
   */
  public static int toInt(Number num, int defaultValue) {
    if (num == null) {
      return defaultValue;
    } else {
      return num.intValue();
    }
  }

  /**
   * 返回float值，为null返回默认值
   *
   * @param num          Float对象
   * @param defaultValue 默认值
   *
   * @return float值
   */
  public static float toFloat(Float num, float defaultValue) {
    if (num == null) {
      return defaultValue;
    } else {
      return num;
    }
  }

  /**
   * boolean值转成整数，true为1，false为0
   */
  public static int toInt(boolean flag) {
    return flag ? 1 : 0;
  }

  /**
   * 字符串转成整型，null或者长度为0的字符串返回null
   *
   * @param str 字符串
   *
   * @return 整型
   */
  public static Integer toInteger(String str) {
    if (str == null || str.length() == 0) {
      return null;
    } else {
      return Integer.parseInt(str);
    }
  }

  /**
   * 返回布尔值，把null转成false
   *
   * @param obj 布尔对象
   *
   * @return boolean
   */
  public static boolean toBool(Boolean obj) {
    if (obj == null) {
      return false;
    } else {
      return obj;
    }
  }

  /**
   * 返回整数值，null转成0
   *
   * @param num Object对象
   *
   * @return 整数值
   */
  // @Deprecated
  public static int toInt(Object num) {
    if (num == null) {
      return 0;
    } else {
      return Integer.parseInt(num.toString());
    }
  }

  /**
   * 返回长整型值，null返回0
   *
   * @param num 长整型对象
   *
   * @return 长整型值
   */
  public static long toLong(Long num) {
    if (num == null) {
      return 0;
    } else {
      return num;
    }
  }

  /**
   * 返回长整型值，null转为0
   *
   * @param num Double对象
   *
   * @return 长整型值
   */
  public static long toLong(Double num) {
    if (num == null) {
      return 0;
    } else {
      return num.longValue();
    }
  }

  /**
   * 返回长整型值，null返回默认值
   *
   * @param num Double对象
   * @param def 默认值
   *
   * @return 长整型值
   */
  public static long toLong(Double num, int def) {
    if (num == null) {
      return def;
    } else {
      return num.longValue();
    }
  }

  /**
   * 计算默认起始记录
   *
   * @param pageId 分页编号
   * @param size   分页大小
   *
   * @return 当前分页的起始记录编号
   */
  public static int getPageStart(int pageId, int size) {
    if (pageId < 1) {
      throw new IllegalArgumentException("pageid不能小于1.");
    }
    if (size < 1) {
      throw new IllegalArgumentException("size不能小于1.");
    }
    return (pageId - 1) * size;
  }

  /**
   * 把null或0转成null
   *
   * @param num 整型对象
   *
   * @return 整型对象
   */
  public static Integer zeroToNull(Integer num) {
    if (num == null) {
      return null;
    }
    if (num == 0) {
      return null;
    }
    return num;
  }

  /**
   * 超过max就置0.
   */
  public static int toZero(int num, int max) {
    if (num > max) {
      return 0;
    }
    return num;
  }

  // /**
  // * 并发数计算.
  // *
  // * @param num1
  // * @param num2
  // * @return
  // */
  // public static int concurrent(int num1, int num2) {
  // double min = num1 > num2 ? num2 : num1;
  // double max = num1 > num2 ? num1 : num2;
  //
  // double ratio = min / max;
  // int count = (int) ((min) / (1 + ratio));
  // // System.out.println("num2:" + num2 + " ratio:" + ratio + " count:" +
  // // count);
  // System.out.println("num:" + num1 + "+" + num2 + "\tavg:" + count);
  // return 0;
  // }

  /**
   * 状态字段解析.
   */
  public static int parseStatus(int status, int num, boolean on) {
    // int typeValue = 0;
    // if (isOn) {
    // status = "status=(status | ?),";
    // typeValue = type.toIntValue();
    // }
    // else {
    // status = "status=(status & ?),";
    // typeValue = ~type.toIntValue();
    // }
    int result;
    if (on) {
      result = status | num;
    } else {
      result = status & ~num;
    }
    System.err.println("result:" + result);
    return result;
  }

  /**
   * 判断是否大于0
   *
   * @param num Double对象
   *
   * @return boolean
   */
  public static boolean isGreaterZero(Double num) {
    if (num == null) {
      return false;
    }
    return num > 0;
  }

  public static boolean isNotGreaterZero(Double num) {
    return !isGreaterZero(num);
  }

  /**
   * 判断是否大于0
   *
   * @param num Long对象
   *
   * @return boolean
   */
  public static boolean isGreaterZero(Long num) {
    if (num == null) {
      return false;
    }
    return num > 0;
  }

  public static boolean isNotGreaterZero(Long num) {
    return !isGreaterZero(num);
  }

  /**
   * 判断是否大于0
   *
   * @param num Long对象
   *
   * @return boolean
   */
  public static boolean isGreaterZero(Integer num) {
    if (num == null) {
      return false;
    }
    return num > 0;
  }

  public static boolean isNotGreaterZero(Integer num) {
    return !isGreaterZero(num);
  }

  /**
   * 格式化十进制数，分隔千位
   *
   * @param num 十进制数
   *
   * @return 格式化后的数值
   */
  public static String format(double num) {
    return new DecimalFormat(",###").format(num);
  }

  public static String format(double num, int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append('0');
    }
    return new DecimalFormat("0." + sb.toString()).format(num);
  }

  /**
   * 保留小数点N位.
   *
   * @param n N位小数点
   */
  public static double scale(double num, int n) {
    double scale = Math.pow(10, n);
    return (int) (num * scale) / scale;
  }

  /**
   * 保留小数点N位.
   *
   * @param n N位小数点
   */
  public static float scale(float num, int n) {
    float scale = (float) Math.pow(10, n);
    // System.out.println("scale:" + scale);
    return ((int) (num * scale)) / scale;
  }

  public static int random(int max) {
    Random random = new Random();
    return random.nextInt(max);
  }

  // region Rate
  public static double getRateDouble(double numerator, double denominator, boolean requestPercentage) {
    BigDecimal numeratorBD = new BigDecimal(String.valueOf(numerator));
    BigDecimal denominatorBD = new BigDecimal(String.valueOf(denominator));
    try {
      double pct = numeratorBD.divide(denominatorBD, MathContext.DECIMAL128).doubleValue();
      if (requestPercentage) {
        pct *= 100;
      }
      return pct;
    } catch (Exception e) {
      return 0;
    }
  }

  public static long getRateLong(long numerator, long denominator, boolean requestPercentage) {
    return (long) getRateDouble(numerator, denominator, requestPercentage);
  }

  public static int getRateInteger(int numerator, int denominator, boolean requestPercentage) {
    return (int) getRateDouble(numerator, denominator, requestPercentage);
  }
  // endregion

}
