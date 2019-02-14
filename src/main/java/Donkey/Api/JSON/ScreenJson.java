package Donkey.Api.JSON;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ScreenJson {
    public String ip;
    public String token;
    public String uuid;
    public String name;
    public int groupId;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    public String message;

    public ScreenJson(){

    }

    public ScreenJson(String ip, String token, String uuid, String name, int groupId, String message){
        this.ip = ip;
        this.token = token;
        this.uuid = uuid;
        this.name = name;
        this.groupId = groupId;
        this.message = message;
    }
}
