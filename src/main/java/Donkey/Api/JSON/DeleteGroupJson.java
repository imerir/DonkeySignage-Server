package Donkey.Api.JSON;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DeleteGroupJson {
    public int id;
    public String name;
    public int parentId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public DeleteGroupJson(){

    }

    public DeleteGroupJson(int id, String name, int parentId, String message){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.message = message;
    }
}
