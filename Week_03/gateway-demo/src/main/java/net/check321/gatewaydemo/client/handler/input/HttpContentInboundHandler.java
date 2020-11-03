package net.check321.gatewaydemo.client.handler.input;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static org.springframework.http.HttpHeaders.*;

/**
 * @author fyang
 * @title Handler for http call response.
 * @description
 * @date 2020/11/3 2:11 下午
 */
@Slf4j
public class HttpContentInboundHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext requestChannelContext;

    public HttpContentInboundHandler(ChannelHandlerContext ctx) {
        this.requestChannelContext = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        String contentType = "";
        if(msg instanceof HttpResponse){
            HttpResponse response = (HttpResponse) msg;
            contentType = response.headers().get(CONTENT_TYPE);
            log.info("Content-type: [{}]",contentType);
        }

        if(msg instanceof HttpContent){
            final HttpContent content = (HttpContent) msg;
            ByteBuf byteBuf = Unpooled.wrappedBuffer(content.content());

            DefaultFullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            String res = byteBuf.toString(Charset.forName("utf8"));

            try {
                response.headers().setInt(CONTENT_LENGTH, res.length());
                response.headers().set(CONTENT_TYPE,contentType);
            } catch (Exception e) {
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            } finally {
                if (ctx.channel().isActive()) {
                    requestChannelContext.writeAndFlush(response);
                }else {
                    requestChannelContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
