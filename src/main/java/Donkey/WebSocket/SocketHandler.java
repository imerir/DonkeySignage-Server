package Donkey.WebSocket;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Tools.Exception.TokenNotMatch;
import Donkey.Tools.Exception.UnknownUUID;
import Donkey.Tools.ScreenTools;
import Donkey.WebSocket.Data.WebSocketData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {

    Logger logger = LogManager.getLogger();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<SocketState.Info> notLogged = SocketState.getINSTANCE().notLogged;
        SocketState.Info info = new SocketState.Info();
        info.socket = session;
        info.connectionDate = LocalDate.now();
        notLogged.add(info);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        ObjectMapper objectMapper = new ObjectMapper();
        WebSocketData data = null;

        WebSocketData response = new WebSocketData();

        Map<String, Object> tmpMap = new HashMap<>();


        try {
            data = objectMapper.readValue(message.getPayload(), WebSocketData.class);
            logger.debug("Receive message on websocket: ");
            logger.debug("Type: " + data.type);
            logger.debug("Data: " + data.data);
            if(data.type.equals("AUTH")){
                response.type = "AUTH";
                ScreenEntity entity = ScreenTools.getInstance().screenLogin(data.data.get("uuid").toString(), data.data.get("token").toString());
                logger.info("WebSocket auth success for " + entity.getName());
                tmpMap.put("status", "ok");
                response.data = tmpMap;

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TokenNotMatch tokenNotMatch) {
            logger.warn("Websocket login, token not match for " + data.data.get("uuid"));
            tmpMap.put("status", "TOKEN_NOT_MATCH");

        } catch (UnknownUUID unknownUUID) {
            logger.warn("Websocket login, Unknown UUID for " + data.data.get("uuid"));
            tmpMap.put("status", "UNKNOWN_UUID");
        }
        response.data = tmpMap;
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
