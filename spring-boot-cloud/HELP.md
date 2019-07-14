# Spring Cloud Eureka



## 配置

spring.application.name=eureka-server
server.port=8761

eureka.instance.hostname=localhost
#使用IP地址，而非hostname方式
#eureka.instance.prefer-ip-address=true

#表示是否注册Eureka服务器,因为自身作为服务注册中心，所以为false
eureka.client.register-with-eureka=false
#是否从eureka上获取注册信息,表明自己是一个server
eureka.client.fetch-registry=false


## 集群配置（多实例）

application.properties中
#使用该文件表示当前有效的配置文件
spring.profiles.active=master

不同的实例不同的配置文件
application-master.properties
application-slave.properties
可以通过eureka.instance.hostname或eureka.instance.ipAddress来区分实例

以上两者eureka.client.serviceUrl.defaultZone相互注册，多个实例时两两注册

*   启动应用需要修改spring.profiles.active或配置debug configuation的profile或打成不同的jar包运行
