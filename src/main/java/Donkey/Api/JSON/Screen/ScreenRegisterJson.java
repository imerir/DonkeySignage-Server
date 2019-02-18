package Donkey.Api.JSON.Screen;

public class ScreenRegisterJson {
    public String token ;
    public String uuid;

    public ScreenRegisterJson(){

    }

    public ScreenRegisterJson(String token, String uuid, String message){
        this.token = token;
        this.uuid = uuid;
    }
}
