package Donkey.Api.JSON.Screen;

public class ModifyScreenJson {
    public int id;
    public String name;
    public int groupId;

    public ModifyScreenJson(){

    }

    public ModifyScreenJson(String name, int groupId) {
        this.name = name;
        this.groupId = groupId;
    }
}
