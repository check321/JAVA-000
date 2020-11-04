package net.check321.gatewaydemo.server.handler.input;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.client.NettyHttpClient;
import net.check321.gatewaydemo.config.Attributes;
import org.springframework.stereotype.Component;

/** 
* @title Forwarding request to target endpoint.
* @description 
* @author fyang 
* @date 2020/11/3 3:41 下午
*/ 
@Component
@Slf4j
@ChannelHandler.Sharable
public class HttpForwardingInboundHandler extends ChannelInboundHandlerAdapter {

    private final NettyHttpClient nettyHttpClient;

    public HttpForwardingInboundHandler(NettyHttpClient nettyHttpClient) {
        this.nettyHttpClient = nettyHttpClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String forwardingPath = ctx.channel().attr(Attributes.ROUTE).get();
        log.info("current route: [{}]",forwardingPath);
        nettyHttpClient.call(ctx,forwardingPath);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
