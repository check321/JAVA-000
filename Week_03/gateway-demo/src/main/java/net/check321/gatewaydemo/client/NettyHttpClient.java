package net.check321.gatewaydemo.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.client.handler.input.HttpContentInboundHandler;
import net.check321.gatewaydemo.config.GatewayConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class NettyHttpClient {

    private final GatewayConfig gatewayConfig;

    public NettyHttpClient(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    public void call(ChannelHandlerContext requestChannelContext, String forwardingStr){

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
                ch.pipeline().addLast(new HttpContentInboundHandler(requestChannelContext));
            }
        });

            final URI forwardingURL = UriComponentsBuilder.fromUriString(forwardingStr).build().toUri();
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET
                    , forwardingURL.getPath());

            request.headers().set(HttpHeaderNames.HOST, forwardingURL.getHost());
            request.headers().set("nio","fyang");

            log.info(request.toString());

            Channel channel = bootstrap.connect(forwardingURL.getHost()
                    , forwardingURL.getPort()).sync().channel();
            // make remote call.
            channel.writeAndFlush(request);
            channel.closeFuture().sync();

        } catch (Exception e) {
           log.error("netty client occurs an error on making remote call.", e);
        }finally {
            workGroup.shutdownGracefully();
        }

    }
    
}
