package Donkey.Api.JSON.Group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//public class ModifyGroupJson {
//    public String oldName;
//    public int oldParentId;
//    public String newName;
//    public int newParentId;
//
//    public ModifyGroupJson(){
//
//    }
//
//    public ModifyGroupJson(String oldName, int oldParentId, String newName, int newParentId){
//        this.oldName = oldName;
//        this.oldParentId = oldParentId;
//        this.newName = newName;
//        this.newParentId = newParentId;
//    }
//
//}
public class ModifyGroupJson {
    public int id;
    public String name;
    public int parentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public ModifyGroupJson(){

    }

    public ModifyGroupJson(String name, int parentId, String message) {
        this.name = name;
        this.parentId = parentId;
        this.message = message;
    }
}
