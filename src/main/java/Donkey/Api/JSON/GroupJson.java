package Donkey.Api.JSON;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class GroupJson {
    public String name;
    public int parent;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    public String message;


    public GroupJson(){

    }

    public GroupJson(String name, int parent,String message){
        this.name = name;
        this.parent = parent;
        this.message = message;
    }
}
