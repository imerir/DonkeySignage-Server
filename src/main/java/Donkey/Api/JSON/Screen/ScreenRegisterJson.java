package Donkey.Api.JSON.Screen;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ScreenRegisterJson {
    public String token ;
    public String uuid;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public ScreenRegisterJson(){

    }

    public ScreenRegisterJson(String token, String uuid, String message){
        this.token = token;
        this.uuid = uuid;
        this.message = message;
    }
}
