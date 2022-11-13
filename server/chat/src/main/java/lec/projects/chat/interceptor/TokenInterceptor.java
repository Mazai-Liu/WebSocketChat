package lec.projects.chat.interceptor;

import lec.projects.chat.enums.MessageAndCode;
import lec.projects.chat.result.Result;
import lec.projects.chat.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private static final String HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod))
            return true;
        log.info("preHandle");
        String token = request.getHeader(HEADER);
        if(!JwtUtil.checkToken(token)){
            response.getWriter().print(Result.fail(MessageAndCode.UNAUTHORIZED));
            return false;
        }

        return true;
    }
}
