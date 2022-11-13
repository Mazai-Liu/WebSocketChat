package lec.projects.chat.config;

import lec.projects.chat.interceptor.WebSocketInterceptor;
import lec.projects.chat.ws.handler.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//@Configuration
//@EnableWebSocket
//public class WebSocketConfig{
//    @Bean
//    public ServerEndpointExporter serverEndpoint() {
//        return new ServerEndpointExporter();
//    }
//
//}
@Configuration
@EnableWebSocket
@EnableWebMvc
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /ws/websocket/log 路径就是前端要访问的路径 类似@ServerEndpoint("/websocket/log")
        //添加处理器、添加拦截地址、添加拦截器
        registry.addHandler(chatWebSocketHandler, "/websocket/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(webSocketInterceptor);
    }
}