# content-storage-app

> 泛用型个人内容存储，采用两步验证来验证用户

## 环境 / Environment
* Spring Boot `ver 2.0.2.RELEASE`
* MyBatis `ver 3.4.6`
* SQLite JDBC `ver 3.23.1`
* Groovy `ver 2.5.0`
* JDK 10 `ver 10.0.1`


## 特性 / Feature
* 动态JSON内容，每个条目允许存储的条目数量是可变的
* 使用Google Authenticator两步验证
* 本机使用，不需联网
* 数据库中的JSON数据加密存储


## TODO
* I18N


## 使用：用户

如果您是普通用户，建议您从[Release](https://github.com/AyakuraYuki/content-storage-app/releases)下载

Linux/macOS使用`Start Application.sh`，Windows使用`Start Application.bat`启动

浏览器访问地址为：
```bash
http://localhost:8888/
```


## 使用：开发者

如果您是开发者，并且有兴趣进行客制化开发和生成，您可以根据下面的说明来使用

### clone
```git
git clone https://github.com/AyakuraYuki/content-storage-app.git
```

### 导入
> 推荐使用IntelliJ IDEA

选择导入maven项目即可

### 初始化SQL

执行SQL代码文件，位置：`<path_to_project>/sql/initialization.sql`

### 修改配置文件

将配置文件中的MySQL连接信息修改为您的SQLite连接信息，可修改属性如下：
```yaml
url: jdbc:sqlite:<your_db_file_path>
username:
password:
```

### 执行程序
```bash
java -jar content-storage-app-<version>.jar
```
