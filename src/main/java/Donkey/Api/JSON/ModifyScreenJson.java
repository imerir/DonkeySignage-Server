package Donkey.Api.JSON;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ModifyScreenJson {
    public int id;
    public String name;
    public int parentId;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    public String message;

    public ModifyScreenJson(){

    }

    public ModifyScreenJson(String name, int parentId, String message) {
        this.name = name;
        this.parentId = parentId;
        this.message = message;
    }
}
