package Donkey.Api.JSON.Screen;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TemporalRegisterJson {
    public String tempToken;
    public String uuid;
    public String expirationDate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public TemporalRegisterJson(){

    }

    public TemporalRegisterJson(String tempToken, String uuid, String expirationDate, String message){
        this.tempToken = tempToken;
        this.uuid = uuid;
        this.expirationDate = expirationDate;
        this.message = message;
    }
}
