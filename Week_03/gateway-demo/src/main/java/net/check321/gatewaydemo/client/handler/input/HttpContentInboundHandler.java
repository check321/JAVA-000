package net.check321.gatewaydemo.client.handler.input;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import lombok.extern.slf4j.Slf4j;

/** 
* @title Handler for http call response.
* @description 
* @author fyang 
* @date 2020/11/3 2:11 下午
*/
@Slf4j
public class HttpContentInboundHandler extends SimpleChannelInboundHandler<HttpContent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpContent msg)  {
        log.info("received response: [{}]", msg.toString());
        ctx.writeAndFlush(msg);
    }

}
