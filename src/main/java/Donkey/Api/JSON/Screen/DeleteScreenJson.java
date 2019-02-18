package Donkey.Api.JSON.Screen;

public class DeleteScreenJson {
    public int id;
    public String name;
    public int groupId;

    public DeleteScreenJson(){

    }

    public DeleteScreenJson(int id, String name, int groupId){
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }
}
