// Built on Wed May 08 12:27:51 CEST 2019 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import java.nio.charset.Charset

scan("10 minutes")
def LOG_HOME = "/Users/ayakurayuki/logs/vertx-playground"

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%-5level]-%logger{36}[%L]: %msg%n"
  }
}

appender("FILE", RollingFileAppender) {
  rollingPolicy(SizeAndTimeBasedRollingPolicy) {
    fileNamePattern = "${LOG_HOME}/log.%d{yyyy-MM-dd}(%i).log"
    maxFileSize = "100MB"
    maxHistory = 60
    totalSizeCap = "2GB"
  }
  encoder(PatternLayoutEncoder) {
    charset = Charset.forName("utf-8")
    pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%p][%c][%M][%L]-> %m%n"
  }
  append = true
  prudent = false
}

root(DEBUG, ["STDOUT"])
logger("cc.ayakurayuki", DEBUG)
logger("com.zaxxer.hikari.pool.HikariPool", INFO)
