package Donkey.Api.JSON.Screen;

public class TemporalRegisterJson {
    public String tempToken;
    public String uuid;
    public String expirationDate;

    public TemporalRegisterJson(){

    }

    public TemporalRegisterJson(String tempToken, String uuid, String expirationDate){
        this.tempToken = tempToken;
        this.uuid = uuid;
        this.expirationDate = expirationDate;
    }
}
