package cc.ayakurayuki.csa.starter.core.util.security

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:47
 */
final enum MessageTypeEnum {

  MD5("MD5"),
  SHA1("SHA-1"),
  SHA256("SHA-256")

  private final String type

  String getType() {
    return type
  }

  MessageTypeEnum(String type) {
    this.type = type
  }

}
