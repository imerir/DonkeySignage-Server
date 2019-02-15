package Donkey.Api.JSON;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DeleteScreenJson {
    public int id;
    public String name;
    public int groupId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public DeleteScreenJson(){

    }

    public DeleteScreenJson(int id, String name, int groupId, String message){
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.message = message;
    }
}
