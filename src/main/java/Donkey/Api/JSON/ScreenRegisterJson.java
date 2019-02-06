package Donkey.Api.JSON;

public class ScreenRegisterJson {
    public String token ;
    public String uuid;

    public ScreenRegisterJson(){

    }

    public ScreenRegisterJson(String token, String uuid){
        this.token = token;
        this.uuid = uuid;
    }
}
