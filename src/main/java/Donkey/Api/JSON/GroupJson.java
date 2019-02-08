package Donkey.Api.JSON;

import java.util.List;

public class GroupJson {
    public String name;
    public int parent;

    public GroupJson(){

    }

    public GroupJson(String name, int parent){
        this.name = name;
        this.parent = parent;
    }
}
