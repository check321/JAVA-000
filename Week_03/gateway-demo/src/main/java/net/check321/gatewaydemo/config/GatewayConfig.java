package net.check321.gatewaydemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayConfig {

    private Server server;

    private List<Route> routes;

    private Map<String,List<String>> routesMap;

    @PostConstruct
    private void init() {
        if (CollectionUtils.isEmpty(routes)) {
            return;
        }

        routesMap = routes.stream()
                .collect(Collectors.toMap(Route::getPath, Route::getUrls));
    }

    @Data
    public static class Server{

        private String host;

        private int port;

    }

    @Data
    public static class Route{

        private String path; // foo-service

        private List<String> urls; // [127.0.0.1,127.0.0.2]

    }
}
