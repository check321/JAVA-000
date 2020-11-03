package net.check321.gatewaydemo.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.client.handler.input.HttpContentInboundHandler;
import net.check321.gatewaydemo.config.GatewayConfig;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component
public class NettyHttpClient {

    private final GatewayConfig gatewayConfig;

    public NettyHttpClient(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    public void call( String targetURL, ByteBuf content){

        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // http protocol parser.
                ch.pipeline().addLast(new HttpResponseDecoder());
                ch.pipeline().addLast(new HttpRequestEncoder());

                // http response handler.
                ch.pipeline().addLast(new HttpContentInboundHandler());
            }
        });

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET
                    , new URI(targetURL).toASCIIString()
                    , content);

            ChannelFuture res = bootstrap.connect(gatewayConfig.getServer().getHost()
                    , gatewayConfig.getServer().getPort()).sync();
            // make remote call.
            res.channel().write(request);
            res.channel().flush();
            res.channel().closeFuture().sync();

        } catch (Exception e) {
           log.error("netty client occurs an error on making remote call.", e);
        }finally {
            workGroup.shutdownGracefully();
        }

    }

}
