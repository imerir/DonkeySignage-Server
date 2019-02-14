package Donkey.Api.JSON;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

public class TemporalRegisterJson {
    public String tempToken;
    public String uuid;
    public String expirationDate;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
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
