package net.check321.gatewaydemo.server.handler.input;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.client.NettyHttpClient;
import net.check321.gatewaydemo.config.Attributes;
import net.check321.gatewaydemo.config.GatewayConfig;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/** 
* @title Forwarding request to target endpoint.
* @description 
* @author fyang 
* @date 2020/11/3 3:41 下午
*/ 
@Component
@Slf4j
@ChannelHandler.Sharable
public class HttpForwardingInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final NettyHttpClient nettyHttpClient;

    private final GatewayConfig gatewayConfig;

    private final String FORWARDING_TEMPLATE = "http://{0}{1}";

    public HttpForwardingInboundHandler(NettyHttpClient nettyHttpClient, GatewayConfig gatewayConfig) {
        this.nettyHttpClient = nettyHttpClient;
        this.gatewayConfig = gatewayConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String route = ctx.channel().attr(Attributes.ROUTE).get();
        log.info("current route: [{}]",route);
        nettyHttpClient.call(MessageFormat.format(FORWARDING_TEMPLATE,route,msg.uri()),msg.content());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
