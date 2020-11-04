# Week_03_NIO&Netty

[TOC]
## Gateway-Demo
基于Netty仿写网关组件，完成路由、过滤、负载等功能。

### 组件结构
> └── net
    └── check321
        └── gatewaydemo 
            ├── GatewayDemoApplication.java  [启动类，借助了SpringIOC，所以依然是个web项目]
            ├── client
            │   ├── NettyHttpClient.java [基于Netty实现的HttpClient，向代理服务发起Http请求]
            │   └── handler
            │       └── input
            │           └── HttpContentInboundHandler.java [NettyHttpClient 响应内容处理器]
            ├── config
            │   ├── Attributes.java [属性配置，用于在ChannelContext里传递参数]
            │   └── GatewayConfig.java [网关配置，可配置路由、负载、过滤器等参数]
            └── server
                ├── GatewayServer.java [网关服务端，用于Accept实际请求]
                └── handler
                    ├── input [组成路由、请求头过滤、负载处理链]
                    │   ├── HttpForwardingInboundHandler.java 
                    │   ├── HttpHeaderInboundHandler.java
                    │   └── RandomRouteInboundHandler.java
                    └── output

```yaml
# Gateway配置
gateway:
  server: 
    host: 127.0.0.1
    port: 7777

# 路由配置
  routes: 
    -
      #  path可做为服务名区分代理服务 
      path: demo
      # urls作为负载列表，实际可替换为注册中心拉取
      urls: 
        - 127.0.0.1:25030
        - 127.0.0.1:25031
        - 127.0.0.1:25032
  # Request-Header 过滤配置
  header:
    key: nio
    value: fyang
```


### 处理流程

- 启动目标服务demo并分别注册于`25030、25031、25032`端口
- 启动网关服务Gateway-Demo监听于`7777`端口
- 模拟用户GET请求通过网关访问demo: 

```
> GET /demo/fyang HTTP/1.1
> Host: 127.0.0.1:7777
> User-Agent: insomnia/2020.4.1
> Accept: */*
```

- `RandomRouteInboundHandler` 路由处理器根据根路由`/demo`拿到已配置的地址列表，并随机选取地址:

> INFO 91197 --- [ntLoopGroup-3-1] n.c.g.s.h.i.RandomRouteInboundHandler    : current root path: [demo]
> INFO 91197 --- [ntLoopGroup-3-1] n.c.g.s.h.i.RandomRouteInboundHandler    : randomize a route for this round: [127.0.0.1:25030]


- `HttpHeaderInboundHandler`请求头过滤器，根据配置文件读取并设置当前请求头

```
GET /demo/fyang HTTP/1.1
host: 127.0.0.1
nio: fyang]
```

- `HttpForwardingInboundHandler`  请求转发处理器，根据以选取的路由地址调用NettyHttpClient向目标服务发起请求

> INFO 91197 --- [ntLoopGroup-3-1] n.c.g.s.h.i.HttpForwardingInboundHandler : current route: [http://127.0.0.1:25030/demo/fyang]

- 获取目标服务响应结果： 

```
hi, fyang
```

## Netty Gateway: BECH 构成

### Bootstrap

#### ServerBootStrap
> A Netty application begins by setting up one of the bootstrap classes, which provide a container for the configuration of the application's network layer

### EventLoop
> An EventLoop processes I/O operations for a Channel. A single EventLoop will typically handle events for multiple Channels. An EventLoopGroup may contain more than one EventLoop and provides an iterator for retrieving the next one in the list.

- 一个`EventLoop`负责处理多个`Channel`的IO连接部分，即处理多个网络连接，为`Channel`提供事件驱动支持

#### EventLoopGroup
- `EventLoopGroup`包含多个`EventLoop`，对于一个Channel只会被`EventLoopGroup`分配同一个`EventLoop`以屏蔽线程安全问题

### Channel
> To be somewhat formal about it, the underlying network transport API must provide applications with a construct tßßßhat implements I/O operations: read, write, connect, bind and so forth. For us, this construct is pretty much always going to be a "socket". Netty's interface Channel defines the semantics for interacting with sockets by way of a rich set of operations: bind, close, config, connect, isActive, isOpen, isWritable, read, write and others. Netty provides numerous Channel implementations for specialized use. These include AbstractChannel, AbstractNioByteChannel, AbstractNioChannel, EmbeddedChannel, LocalServerChannel, NioSocketChannel and many more.

- Netty对OS层面的连接Socket封装增强
- 将Socket基本动作事件化（isOpen, isActive）
- 基于不同场景提供特殊实现(NioSocketChannel )


#### ChannelHandler

> ChannelHandlers support a variety of protocols and provide containers for data-processing logic. We have already seen that a ChannelHandler is triggered by a specific event or set of events. Note that the use of the generic term "event" is intentional, since a ChannelHandler can be dedicated to almost any kind of action - converting an object to bytes (or the reverse), or handling exceptions thrown during processing.
One interface you’ll be encountering (and implementing) frequently is ChannelInboundHandler. This type receives inbound events (including received data) that will be handled by your application's business logic. You can also flush data from a ChannelInboundHandler when you have to provide a response. In short, the business logic of your application typically lives in one or more ChannelInboundHandlers.

- 不同SocketChannel具体处理实现(基于不同网络协议封装)
- 与`Event`绑定并提供功能增强，如`ChannelInboundHandler`基于`receive`事件对Socket入参数实现bytes —> object 反序列化支持

#### ChannelPipline

> A ChannelPipeline provides a container for a chain of ChannelHandlers and presents an API for managing the flow of inbound and outbound events along the chain. Each Channel has its own ChannelPipeline, created automatically when the Channel is created.
How do ChannelHandlers get installed in the ChannelPipeline? This is the role of abstract ChannelInitializer, which implements ChannelHandler. A subclass of ChannelInitializer is registered with a ServerBootstrap. When its method initChannel() is called, this object will install a custom set of ChannelHandlers in the pipeline. When this operation is completed, the ChannelInitializer subclass then automatically removes itself from the ChannelPipeline.

- 将`ChannelHandler`链式组合成自定义的业务处理流程，这个容器抽象为`ChannelHandlerPipline`.
- `ChannelHandlerPipline`提供管理整个`ChannelHandler`生命周期与暴露统一API的作用.
- 通过实现`ChannelInitializer`的`initChannel()`将`ChannelHandler`集合组成`ChannelHandlerPipline`
- `ChannelInitializer` 注册于`BootStrap`

> ChannelHandler(s) -> ChannelPipline — [ChannelInitializer] —> ServerBootStrap







