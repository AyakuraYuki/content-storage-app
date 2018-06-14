package cc.ayakurayuki.contentstorage.common.util.security

/**
 * Created by Ayakura Yuki on 2017/7/5.
 */
enum MessageTypeEnum {

  MD5("MD5"),
  SHA1("SHA-1"),
  SHA256("SHA-256")

  private String type

  String getType() {
    return type
  }

  MessageTypeEnum(String type) {
    this.type = type
  }

}
