package cc.ayakurayuki.csa.starter.core.annotation

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.METHOD
import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 *
 * @author ayakurayuki* @date 2019/06/05-18:00
 */
@Documented
@Retention(RUNTIME)
@Target([TYPE, METHOD])
@interface Token {
}
