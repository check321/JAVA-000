package net.check321.gatewaydemo.server.handler.input;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import net.check321.gatewaydemo.config.Attributes;
import net.check321.gatewaydemo.config.GatewayConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@ChannelHandler.Sharable
public class RandomRouteInboundHandler extends ChannelInboundHandlerAdapter {

    private final GatewayConfig gatewayConfig;

    public RandomRouteInboundHandler(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        final FullHttpRequest request = (FullHttpRequest) msg;

        // find the root path for routes-map.
        String rootPath = UriComponentsBuilder.fromPath(request.uri()).build().getPathSegments().get(0);
        if("favicon.ico".equals(rootPath)){
            ctx.close();
            return;
        }

        log.info("current root path: [{}]",rootPath);
        
        List<String> routes = gatewayConfig.getRoutesMap().get(rootPath);
        if(CollectionUtils.isEmpty(routes)){
            log.error("configure routes map first.");
            ctx.close();
        }

        String route = routes.get(new Random().nextInt(routes.size()));
        log.info("randomize a route for this round: [{}]" ,route);

        final String forwardingPath = UriComponentsBuilder.fromPath(request.uri())
                .scheme("http")
                .host(route)
                .build()
                .toString();

        ctx.channel().attr(Attributes.ROUTE).set(forwardingPath);

        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }

    public static void main(String[] args) {
        final String forwardingPath = UriComponentsBuilder.fromPath("/demo/fyang")
                .scheme("http")
                .host("127.0.0.1")
                .build()
                .toUriString();

        System.out.println(forwardingPath);
    }
}
