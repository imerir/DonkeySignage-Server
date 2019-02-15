package Donkey.Api.JSON;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ModifyScreenJson {
    public int id;
    public String name;
    public int groupId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public ModifyScreenJson(){

    }

    public ModifyScreenJson(String name, int groupId, String message) {
        this.name = name;
        this.groupId = groupId;
        this.message = message;
    }
}
