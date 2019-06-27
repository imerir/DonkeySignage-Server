package Donkey.WebSocket;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.SpringContext;
import Donkey.Tools.Exception.TokenNotMatch;
import Donkey.Tools.Exception.UnknownUUID;
import Donkey.Tools.ScreenTools;
import Donkey.WebSocket.Data.WebSocketData;
import Donkey.Widgets.WidgetInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Handler for screen socket connection
 */
@Component
public class SocketHandler extends TextWebSocketHandler {

    private Logger logger = LogManager.getLogger();
    private ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketUtils webSocketUtils = WebSocketUtils.getINSTANCE();



    public SocketHandler(){

    }


    /**
     * When connection is Established, register the screen
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<SocketState.Info> notLogged = SocketState.getINSTANCE().notLogged;
        SocketState.Info info = new SocketState.Info();
        info.socket = session;
        info.connectionDate = LocalDate.now();
        notLogged.add(info);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        WebSocketData data = null;

        WebSocketData response = new WebSocketData();

        Map<String, Object> tmpMap = new HashMap<>();


        try {
            data = objectMapper.readValue(message.getPayload(), WebSocketData.class);
            logger.debug("Receive message on websocket: ");
            logger.debug("Type: " + data.type);
            logger.debug("Data: " + data.data);
            switch (data.type){
                case "AUTH":
                    response.type = "AUTH";
                    try {
                        ScreenEntity entity = ScreenTools.getInstance().screenLogin(data.data.get("uuid").toString(), data.data.get("token").toString());
                        logger.info("WebSocket auth success for " + entity.getName());
                        tmpMap.put("status", "ok");
                        response.data = tmpMap;
                        SocketState states = SocketState.getINSTANCE();
                        SocketState.Info noLoggedState = states.getNotLoggedBySocket(session);
                        noLoggedState.uuid = entity.getUuid();
                        noLoggedState.connectionDate = LocalDate.now();
                        states.logged.put(entity.getUuid(), noLoggedState);
                        states.notLogged.remove(noLoggedState);
                    } catch (TokenNotMatch tokenNotMatch) {
                        logger.warn("Websocket login, token not match for " + data.data.get("uuid"));
                        tmpMap.put("status", "TOKEN_NOT_MATCH");

                    } catch (UnknownUUID unknownUUID) {
                        logger.warn("Websocket login, Unknown UUID for " + data.data.get("uuid"));
                        tmpMap.put("status", "UNKNOWN_UUID");
                    }
                    response.data = tmpMap;
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                    webSocketUtils.sendWidgetManifest(session);
                    break;

                case "CONFIG":
                    new Thread(() -> {
                        try {
                            webSocketUtils.sendConfig(session);
                        } catch (IOException e) {
                            logger.catching(e);
                        }
                    }).start();

                    break;

            }
        } catch (IOException e) {
            logger.catching(e);
        }


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("Socket disconnect !");
        SocketState states = SocketState.getINSTANCE();
        SocketState.Info nLogged = states.getNotLoggedBySocket(session);
        if (nLogged != null) {
            logger.debug("Socket found on notLogged list");
            states.notLogged.remove(nLogged);
        } else {
            SocketState.Info logged = states.getLoggedBySocket(session);
            if (logged != null) {
                logger.debug("Socket found on logged list");
                states.logged.values().remove(logged);
            } else {
                logger.error("Can't remove Socket from lists, socket can't be found !");
            }

        }


    }






}
