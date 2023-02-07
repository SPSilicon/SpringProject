package com.hig.hwangingyu.config;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.hig.hwangingyu.handler.SocketWebmHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SocketWebmHandler socketWebmHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( socketWebmHandler,"/stream/share/{token}/{listen}")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                        System.out.println("Request ----------" +request.getHeaders());
                        
                        String path = request.getURI().getPath();
                        System.out.println("PATH  " + path);
                        int st = path.lastIndexOf("share/")+6;
                        int index = path.lastIndexOf("/");
                        String streamName = path.substring(st,index);
                        String listen = path.substring(index+1);
                        attributes.put("streamName", streamName);
                        attributes.put("listen",listen);
                        

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
