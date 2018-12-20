# content-storage-app

> 泛用型个人内容存储，采用两步验证来验证用户

## 环境 / Environment
* Spring Boot `ver 2.0.2.RELEASE`
* MyBatis `ver 3.4.6`
* SQLite JDBC `ver 3.23.1`
* Groovy `ver 2.5.0`
* JDK 1.8 `ver 1.8.0_181`

## 特性 / Feature
* JSON化存储内容
* 使用Google Authenticator两步验证
* 单机使用
* 数据加密后持久化
* DB文件级唯一的DES Key

## 使用：用户

如果您是普通用户，建议您从[Release](https://github.com/AyakuraYuki/content-storage-app/releases)下载

Linux/macOS使用`startup.sh`，Windows使用`startup.bat`启动

浏览器访问地址：`http://localhost:8888/`

## 使用：开发者

如果您是开发者，并且有兴趣进行客制化开发和生成，您可以根据下面的说明来使用

### clone
```git
git clone https://github.com/AyakuraYuki/content-storage-app.git
```

### 导入
> 推荐使用IntelliJ IDEA

选择导入maven项目即可

### 修改配置文件

将配置文件中的MySQL连接信息修改为您的SQLite连接信息，可修改属性如下：
```yaml
url: jdbc:sqlite:< _your_db_file_path_ || data/storage.db >
```

### 执行程序
```bash
(java -jar content-storage-app-<version>.jar &)
```

## Special Thanks to

<div><img src="https://blog.ayakurayuki.cc/assets/img/jetbrains/variant-2_logos/jetbrains-variant-2.png" alt="JetBrains" width="256px" height="256px"/></div>
* [JetBrains: Developer Tools for Professionals and Teams](https://www.jetbrains.com/?from=content-storage-app)
