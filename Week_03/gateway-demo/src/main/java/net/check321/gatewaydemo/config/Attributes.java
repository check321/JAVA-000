package net.check321.gatewaydemo.config;

import io.netty.util.AttributeKey;

public interface Attributes {

   AttributeKey<String> ROUTE = AttributeKey.newInstance("route");

   AttributeKey<GatewayConfig.Header> HEADER = AttributeKey.newInstance("header");
}
