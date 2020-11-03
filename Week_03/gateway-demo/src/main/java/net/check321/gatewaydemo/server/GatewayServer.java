package net.check321.gatewaydemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.config.GatewayConfig;
import net.check321.gatewaydemo.server.handler.input.HttpForwardingInboundHandler;
import net.check321.gatewaydemo.server.handler.input.RandomRouteInboundHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class GatewayServer {

    private final RandomRouteInboundHandler randomRouteInboundHandler;

    private final HttpForwardingInboundHandler httpForwardingInboundHandler;

    private final GatewayConfig gatewayConfig;

    public GatewayServer(GatewayConfig gatewayConfig, RandomRouteInboundHandler randomRouteInboundHandler, HttpForwardingInboundHandler httpForwardingInboundHandler) {
        this.gatewayConfig = gatewayConfig;
        this.randomRouteInboundHandler = randomRouteInboundHandler;
        this.httpForwardingInboundHandler = httpForwardingInboundHandler;
    }

    @PostConstruct
    public void listening(){
        start();
    }

    public void start(){

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                            ch.pipeline().addLast(randomRouteInboundHandler); // routing.
                            ch.pipeline().addLast(httpForwardingInboundHandler);// forwarding.
                        }
                    });

            GatewayConfig.Server server = gatewayConfig.getServer();
            Channel channel = boot.bind(server.getPort())
                    .sync()
                    .channel();

            log.info("gateway is listening on : [{}/{}]",server.getHost(),server.getPort());
            channel.closeFuture().sync();

        }catch (Exception e){
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}
