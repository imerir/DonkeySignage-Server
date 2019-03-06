package Donkey.Api.JSON.Group;

public class GroupJson {
    public int id;
    public String name;
    public int parentId;

    public GroupJson(){

    }

    public GroupJson(int id, String name, int parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
