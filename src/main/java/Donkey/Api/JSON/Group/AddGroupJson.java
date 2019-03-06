package Donkey.Api.JSON.Group;

public class AddGroupJson {
    public String name;
    public int parent;


    public AddGroupJson(){

    }

    public AddGroupJson(String name, int parent){
        this.name = name;
        this.parent = parent;
    }
}
