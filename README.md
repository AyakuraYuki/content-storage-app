# content-storage-app
> 泛用型个人内容存储，采用两步验证来验证用户

## 环境 / Environment
* Spring Boot 1.5.7.RELEASE
* MyBatis 3.4.5
* MySQL 5.7.18
* Support SQLite3
* Groovy 2.4.12
* Base on JDK 1.8.0_144

## 特性 / Feature
* 动态JSON内容，每个条目允许存储的条目数量是可变的
* 使用Google Authenticator两步验证，配置简单
* 本机使用，不需联网
* 数据库中的JSON数据采用DES加密，所以大可不必担心数据被解密泄露

## TODO
* I18N

## 使用：用户
> 请注意，普通用户版本将关闭MySQL支持，使用SQLite3存储内容

如果您是普通用户，建议您从[Release](https://github.com/AyakuraYuki/content-storage-app/releases)下载使用。

浏览器访问地址为：
```bash
http://localhost:8888/
```

## 使用：开发者
如果您是开发者，并且有兴趣进行客制化开发和生成，您可以根据下面的说明来使用

### clone项目
```git
git clone https://github.com/AyakuraYuki/content-storage-app.git
```

### 导入
> 推荐使用IntelliJ IDEA

选择导入maven项目即可

### 初始化MySQL
执行SQL代码文件，位置：sql/initialization.sql

### 修改配置文件
将配置文件中的MySQL连接信息修改为您的MySQL/SQLite连接信息，可修改属性如下：
```yaml
# MySQL
url: jdbc:mysql://<host:port>/content_storage?useUnicode=true&characterEncoding=UTF-8
username: <your_username>
password: <your_password>

# SQLite：需要注释MySQL的四条属性，并取消以下属性的配置
# driver-class-name: org.sqlite.JDBC
# url: jdbc:sqlite:<your_db_file_path>
# username:
# password:
# Hibernate-SQLite support
# jpa:
cc.ayakurayuki.contentscc.ayakurayuki.contentstorage.common.dialect#     ddl-auto: update
#   show-sql: false
```

### 执行程序
```bash
java -jar content-storage-app-x.x.x.jar
```