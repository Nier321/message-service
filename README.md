message-service

即时消息子系统，基于 Spring Boot，支持多用户聊天室、消息加密传输、WebSocket 实时通信、(RocketMQ 消息队列)。适合招聘、社交等需要高效消息交互的场景。

项目亮点

支持聊天室创建、加入、消息收发、在线人数等功能

RESTful API + WebSocket 实时推送

支持多用户并发、线程安全

// RocketMQ 实现异步消息分发

自定义注解 + AOP 实现消息加解密，提升安全性

完善的日志与配置管理

技术栈

Java 18
Spring Boot 2.5.x
Spring Web、WebSocket
// RocketMQ
Redis（开发中）
Lombok、Jackson
前端：HTML/CSS/JavaScript

核心功能展示

![image](https://github.com/user-attachments/assets/cb69e9a8-8f10-4e38-bb9f-1e1b3775ec62)

![image](https://github.com/user-attachments/assets/640c5e7b-2b5c-4492-b2ee-a203f483f970)

![image](https://github.com/user-attachments/assets/ed409607-b374-45ad-8c74-647de8f9cd2f)


本地部署

1.克隆仓库

			git clone https://github.com/Nier321/message-service.git
			cd **/message-service
2.配置 Redis、RocketMQ 服务（如果需要的话），并修改 application.yml

3.构建并启动

			mvn clean install
			mvn spring-boot:run
4.访问

页面: http://localhost:8080/index.html
																																																				
