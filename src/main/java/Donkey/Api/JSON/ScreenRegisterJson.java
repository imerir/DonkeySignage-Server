package Donkey.Api.JSON;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ScreenRegisterJson {
    public String token ;
    public String uuid;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    public String message;

    public ScreenRegisterJson(){

    }

    public ScreenRegisterJson(String token, String uuid, String message){
        this.token = token;
        this.uuid = uuid;
        this.message = message;
    }
}
