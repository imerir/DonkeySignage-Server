package Donkey.Api.JSON.Screen;

public class ScreenJson {
    public int id;
    public String name;
    public int groupId;

    public ScreenJson(){

    }

    public ScreenJson(int id, String name, int groupId){
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }
}
