package cc.ayakurayuki.contentstorage.common.util

import org.yaml.snakeyaml.Yaml

import java.sql.DriverManager

/**
 * Created by ayakurayuki on 2018/3/7-15:35
 * <p>
 * Package: cc.ayakurayuki.contentstorage.common.util
 */
class DBInitUtils {

  static def dbInit() {
    // Get Spring Boot application configure file.
    def filepath = DBInitUtils.class.classLoader.getResource('application.yml').file
    // Transfer configure file to YAML object.
    def yaml = new Yaml()
    def config = yaml.loadAs(new File(filepath).newReader(), Map)
    // Get JDBC URL config.
    def jdbcURL = config['spring']['datasource']['url'] as String
    def absolutePath = jdbcURL.replace(jdbcURL.substring(0, jdbcURL.lastIndexOf(':') + 1), "${System.getProperty("user.dir")}/")
    def dbFile = new File(absolutePath)
    if (dbFile.exists()) {
      println """
[DBInitUtils] JDBC URL: ${jdbcURL}
[DBInitUtils] DB file path: ${absolutePath}
[DBInitUtils] SQLite database file is exists.
[DBInitUtils] Database initialization completed.
        """.trim()
      return
    }
    def dbPath = absolutePath.substring(0, absolutePath.lastIndexOf('/') + 1)
    new File(dbPath).mkdirs()
    dbFile.createNewFile()
    Class.forName('org.sqlite.JDBC')
    def connection = DriverManager.getConnection(jdbcURL)
    def statement = connection.createStatement()
    def result = statement.executeUpdate("""
create table main.content (
  id        text primary key,
  item      text not null,
  json_data text
);

create table main.`settings` (
  id    text primary key,
  key   text not null,
  value text
);
      """.trim())
    println """
[DBInitUtils] JDBC URL: ${jdbcURL}
[DBInitUtils] absolutePath: ${absolutePath}
[DBInitUtils] SQLite database file is created. Statement result is ${result}.
[DBInitUtils] Database initialization completed.
      """.trim()
    statement.close()
    connection.close()
  }

}
