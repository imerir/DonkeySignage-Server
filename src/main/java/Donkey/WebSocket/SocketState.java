package Donkey.WebSocket;

import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SocketState {
    private static SocketState INSTANCE = new SocketState();
    public static SocketState getINSTANCE() {return INSTANCE;}

    private SocketState(){}


    HashMap<String, Info> logged = new HashMap<>();
    List<Info> notLogged = new ArrayList<>();



    public Info getNotLoggedBySocket(WebSocketSession session){
        for(Info info : notLogged){
            if( info.socket == session){
                return info;
            }
        }
        return null;
    }

    public Info getLoggedBySocket(WebSocketSession session){
        for(Info info : logged.values()){
            if( info.socket == session){
                return info;
            }
        }
        return null;
    }




    public static class Info {
        public WebSocketSession socket;
        public String uuid;
        public LocalDate connectionDate;
        public LocalDate loginDate;
    }



}
