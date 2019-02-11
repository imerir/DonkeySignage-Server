package Donkey.Api.JSON;

public class DeleteGroupJson {
    public int id;
    public String name;
    public int parentId;

    public DeleteGroupJson(){

    }

    public DeleteGroupJson(int id, String name, int parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
