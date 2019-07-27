package cc.ayakurayuki.csa.starter.core.util

/**
 *
 * @author ayakurayuki* @date 2019/05/21-10:56
 */
final class IdUtils {

  def static UUID() {
    UUID.randomUUID().toString().replaceAll("-", "").toUpperCase()
  }

}
