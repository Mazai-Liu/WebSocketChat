package lec.projects.chat.ws.handler;

import com.alibaba.fastjson.JSON;
import lec.projects.chat.dto.NameAndAvatar;
import lec.projects.chat.entity.Record;
import lec.projects.chat.message.ResultMessage;

import lec.projects.chat.service.RecordService;
import lec.projects.chat.service.UserService;
import lec.projects.chat.utils.SensitiveWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static Map<String,WebSocketSession> USER2SESSION = new ConcurrentHashMap<>();

    private static Map<WebSocketSession, String> session2username = new ConcurrentHashMap<>();

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 在线人数
    public static Integer headcount = 0;

    @Autowired
    private RecordService recordService;
    @Autowired
    private SensitiveWordUtil sensitiveWordUtil;
    @Autowired
    private UserService userService;

    /**
     * 连接建立后保存用户的登录状态
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("连接" + session + "建立");
        String username = (String) session.getAttributes().get("username");

        if(USER2SESSION.containsKey(username)) {
            try {
                WebSocketSession webSocketSession = USER2SESSION.get(username);
                webSocketSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        USER2SESSION.put(username,session);
        session2username.put(session,username);

        log.info(username + "登录了");

        headcount++;

        listUsers();
    }

    /**
     * 向所有websocket客户端更新在线用户
     */
    private void listUsers(){
        ResultMessage resultMessage = new ResultMessage(false,true,false,null,headcount,getNamesAndAvatars());
        String message = JSON.toJSONString(resultMessage);
        System.out.println(message);
        broadcast(message);
    }

    /**
     * 获取在线用户的名字和头像用以展示
     * @return
     */
    public List<NameAndAvatar> getNamesAndAvatars(){
        Set<String> names = USER2SESSION.keySet();

        // getAvatars
        List<NameAndAvatar> nameAndAvatars = userService.getAvatarsByNames(names);

        // 公共聊天室
        if(nameAndAvatars != null)
            nameAndAvatars.add(new NameAndAvatar("Lec","img/chat/avatar/lec.png"));

        log.info("{}",nameAndAvatars);
        return nameAndAvatars;
    }

    /**
     * 向所有websocket客户端广播消息
     * @param message
     */
    private void broadcast(String message){
        Set<String> names = USER2SESSION.keySet();
        for(String name : names){
            WebSocketSession session = USER2SESSION.get(name);
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到发送的聊天消息
     * @param session 发送者的 WebSocketSession
     * @param message 消息
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String username = session2username.get(session);
        log.info("WebSocket Server receive:" + message.getPayload());
        Record record = JSON.parseObject(message.getPayload().toString(), Record.class);
        record.setFromName(session2username.get(session));
        record.setTime(dateFormat.format(new Date()));

        String toName = record.getToName();
        String content = record.getContent();
        record.setContent(sensitiveWordUtil.replaceSensitiveWord(content,1,"*"));

        // 新增聊天记录
        recordService.insertRecord(record);

        ResultMessage resultMessage = new ResultMessage(false,false,true,username,headcount,record);
        String json = JSON.toJSONString(resultMessage);

        if(!toName.equals("Lec")){
            WebSocketSession toSession = USER2SESSION.get(toName);

            toSession.sendMessage(new TextMessage(json));
            session.sendMessage(new TextMessage(json));

            log.info(username + "发送了:" + content + "给" + toName);
        }else{
           broadcast(json);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    /**
     * 连接关闭后，更新用户在线状态
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session2username.get(session);

        USER2SESSION.remove(username);
        session2username.remove(session);
        session.close();

        log.info("连接" + session + "关闭," + username + "退出");

        headcount--;
        listUsers();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
