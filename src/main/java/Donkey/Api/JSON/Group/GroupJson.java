package Donkey.Api.JSON.Group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class GroupJson {
    public String name;
    public int parent;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;


    public GroupJson(){

    }

    public GroupJson(String name, int parent,String message){
        this.name = name;
        this.parent = parent;
        this.message = message;
    }
}
