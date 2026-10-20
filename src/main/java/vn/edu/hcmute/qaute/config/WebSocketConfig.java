package vn.edu.hcmute.qaute.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import vn.edu.hcmute.qaute.websocket.StompChannelInterceptor;
import vn.edu.hcmute.qaute.websocket.WsHandshakeHandler;
import vn.edu.hcmute.qaute.websocket.WsHandshakeInterceptor;
import vn.edu.hcmute.qaute.websocket.WsSessionDecoratorFactory;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final AppProperties properties;
    private final WsHandshakeInterceptor handshakeInterceptor;
    private final WsHandshakeHandler handshakeHandler;
    private final StompChannelInterceptor channelInterceptor;
    private final WsSessionDecoratorFactory decoratorFactory;

    public WebSocketConfig(AppProperties properties, WsHandshakeInterceptor handshakeInterceptor,
            WsHandshakeHandler handshakeHandler, StompChannelInterceptor channelInterceptor,
            WsSessionDecoratorFactory decoratorFactory) {
        this.properties = properties;
        this.handshakeInterceptor = handshakeInterceptor;
        this.handshakeHandler = handshakeHandler;
        this.channelInterceptor = channelInterceptor;
        this.decoratorFactory = decoratorFactory;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        List<String> origins = new ArrayList<>(List.of("http://localhost:*", "https://localhost:*",
                "http://127.0.0.1:*", "https://127.0.0.1:*"));
        if (properties.getBaseUrl() != null && !properties.getBaseUrl().isBlank()) {
            URI base = URI.create(properties.getBaseUrl());
            if (base.getScheme() != null && base.getHost() != null) {
                origins.add(base.getScheme() + "://" + base.getHost()
                        + (base.getPort() < 0 ? "" : ":" + base.getPort()));
            }
        }
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(origins.toArray(String[]::new))
                .addInterceptors(handshakeInterceptor)
                .setHandshakeHandler(handshakeHandler)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(16 * 1024);
        registration.setSendTimeLimit(20_000);
        registration.setSendBufferSizeLimit(512 * 1024);
        registration.addDecoratorFactory(decoratorFactory);
    }
}
