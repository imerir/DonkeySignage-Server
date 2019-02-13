package Donkey.Api.JSON;

public class ModifyGroupJson {
    public String oldName;
    public int oldParentId;
    public String newName;
    public int newParentId;

    public ModifyGroupJson(){

    }

    public ModifyGroupJson(String oldName, int oldParentId, String newName, int newParentId){
        this.oldName = oldName;
        this.oldParentId = oldParentId;
        this.newName = newName;
        this.newParentId = newParentId;
    }

}
