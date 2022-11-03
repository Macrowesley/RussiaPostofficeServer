# easy patent项目
启动需要先开启redis，mongodb

启动mongodb指令
mongod --dbpath /usr/local/mongodb5.0/data --logpath /usr/local/mongodb5.0/log/mongod.log --logappend

## 关于税率版本
俄罗斯发新的税率版本给我们服务器，我们通知所有机器
新增机器的时候，我们出场的时候，把已有的税率版本都刷进新机器中


## 在公司如何远程操作机器？
1. 机器在服务器的数据表中有记录
2. 服务器打开桌面frp快捷方式
3. 机器开机，登录服务器
4. 登录网页后台，点击ssh连接按钮
5. 机器可以成功远程升级
6. 关闭frp服务


项目中暂时去除了IP地址、Mac地址等其他信息校验，由于License中可被允许的参数信息不知道在哪设置。
登录信息：
管理员admin#111111
机构jigou#111111
审核shenhe#111111
国际化暂时去除了中文之外的语言，需要数据字段支持

