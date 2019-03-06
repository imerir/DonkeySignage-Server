package Donkey.Api.JSON.Screen;

public class AddScreenJson {
    public String ip;
    public String token;
    public String uuid;
    public String name;
    public int groupId;

    public AddScreenJson(){

    }

    public AddScreenJson(String ip, String token, String uuid, String name, int groupId){
        this.ip = ip;
        this.token = token;
        this.uuid = uuid;
        this.name = name;
        this.groupId = groupId;
    }
}
