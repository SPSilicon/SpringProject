package com.hig.hwangingyu.config;

import java.util.Map;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.hig.hwangingyu.sockethandler.SocketWebmHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketWebmHandler(), "/stream/share/{token}")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        // TODO Auto-generated method stub

                        System.out.println("Request ----------" +request.getHeaders());

                        String path = request.getURI().getPath();
                        System.out.println("PATH  " + path);
                        
                        int index = path.lastIndexOf("/");
                        String streamName = path.substring(index+1);
                        attributes.put("streamName", streamName);

                        return super.beforeHandshake(request, response, wsHandler, attributes);
                        
                    }

                });

    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(65565);
        container.setMaxBinaryMessageBufferSize(10000000);
        return container;
    }

}
