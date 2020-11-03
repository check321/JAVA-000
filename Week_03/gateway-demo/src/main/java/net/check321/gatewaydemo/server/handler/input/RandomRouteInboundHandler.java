package net.check321.gatewaydemo.server.handler.input;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.config.Attributes;
import net.check321.gatewaydemo.config.GatewayConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@ChannelHandler.Sharable
public class RandomRouteInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final GatewayConfig gatewayConfig;

    public RandomRouteInboundHandler(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        String rootPath = msg.uri().substring(msg.uri().indexOf("/"),msg.uri().lastIndexOf("/"));

        log.info("current root path: [{}]",rootPath);

        List<String> routes = gatewayConfig.getRoutesMap().get(rootPath);
        if(CollectionUtils.isEmpty(routes)){
            log.error("configure routes map first.");
            ctx.close();
            return;
        }

        String route = routes.get(new Random().nextInt(routes.size()));
        log.info("current route: [{}]" ,route);

        ctx.channel().attr(Attributes.ROUTE).set(route);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
