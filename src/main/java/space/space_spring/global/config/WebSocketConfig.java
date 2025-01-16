package space.space_spring.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import space.space_spring.global.interceptor.jwtSocket.JwtChannelInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp 접속 주소 url = ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지 구독(수신) 요청의 엔드포인트
        registry.enableSimpleBroker("/topic");

        // 메세지 발행(송신) 요청의 엔드 포인트
        registry.setApplicationDestinationPrefixes("/app", "/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(50 * 1024 * 1024);  // 50MB
        registration.setSendBufferSizeLimit(50 * 1024 * 1024);  // 50MB
        registration.setSendTimeLimit(20 * 10000);  // 더 긴 시간 제한 설정
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean factoryBean = new ServletServerContainerFactoryBean();
        factoryBean.setMaxTextMessageBufferSize(50 * 1028 * 1024);
        factoryBean.setMaxBinaryMessageBufferSize(50 * 1028 * 1024);
        factoryBean.setMaxSessionIdleTimeout(20 * 10000L);
        factoryBean.setAsyncSendTimeout(20 * 10000L);
        return factoryBean;
    }
}
