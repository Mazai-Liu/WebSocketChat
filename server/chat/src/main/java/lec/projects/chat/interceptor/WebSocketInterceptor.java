package lec.projects.chat.interceptor;

import io.jsonwebtoken.Jwt;
import lec.projects.chat.enums.MessageAndCode;
import lec.projects.chat.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info("Before Handshake");
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse)response).getServletResponse();
        String token = httpServletRequest.getParameter("token");
        log.info("token:" + token);

        // check token
        if(StringUtils.isEmpty(token) || !JwtUtil.checkToken(token)){
            log.info("未授权的WebSocket连接请求");
            httpServletResponse.setStatus(Integer.parseInt(MessageAndCode.WSUNAUTHORIZED.getCode()));
            return false;
        }

        String id = JwtUtil.getClaim("id",token);
        String username = JwtUtil.getClaim("username",token);
        log.info("token用户为{}, id为{}",username, id);

        attributes.put("id",id);
        attributes.put("username",username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
