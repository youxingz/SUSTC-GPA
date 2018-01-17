# SUSTC_GPA
南方科技大学 GPA 查询系统

[![License](https://poser.pugx.org/phpunit/phpunit/license)](https://packagist.org/packages/phpunit/phpunit)

该系统只提供给南科大学生访问学期成绩并查询 GPA 所用，不做它途。任何人可下载源码进行修改、扩展功能，但作者不对任何修改版负责。本系统承诺除了基础的流量统计之外不获取任何私人信息（如：成绩），任何因利用该系统修改的功能来获取用户信息的行为所产生的后果不由作者负责。

static/../vendors/ 目录下所有的第三方引用内容都尽可能提供和保留了官方版源代码及说明文档。

## 技术支持
1. Framework：SpringBoot 1.4.3.RELEASE 版本
2. 项目管理工具：Maven
3. 数据库：mySQL 5.6
4. ORM：MyBatis

### Update v3.0
#### 2017/10/20 
修复 CAS 升级后登录问题

最新版本（v3.0）代码在 com.gpaer.service.v3 下面，旧版系统同步更新于 com.gpaer.service.v3_plain 下。

注：CAS Https 模拟登录采用 Apache HttpClient 进行模拟客户端请求，并不再验证 ssl 证书。

### Update v2.0
#### 2017/03/15
升级版本，界面更新为 Material Design 风格

提供分学期查询，并包含个人信息显示

### Publish v1.0
#### 2017/01/21
发布第一版本，提供个人成绩查询、GPA 计算功能

模拟 CAS 登录


## 最新更新 2017/10/20 Thu
