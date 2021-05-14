# **Admin管理框架Springboot后台服务**

## 项目依赖
由于使用了Lombok，IDE需要安装Lombok插件

需要额外依赖项目<br>
cn.solwind.dbgenerator<br>
cn.solwind.common

## DB
数据库建表语句在doc/DB中<br>
新表可以通过修改/resources/mybatis-generator.xml中数据库配置，然后使用Maven插件exec:java反向生成entity和Mapper<br>
_注意：需要依赖cn.solwind.dbgenerator，已解决java文件合并问题，增加Lombok和TK.Mapper支持_

## Swagger访问地址
http://127.0.0.1:{port}/swagger-ui/index.html

## 需注意的问题

