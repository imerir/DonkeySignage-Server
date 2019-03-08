package Donkey.Api.JSON.Group;

import Donkey.Database.Entity.GroupEntity;

public class GroupJson {
    public int id;
    public String name;
    public int parentId;

    public GroupJson(){

    }

    public GroupJson(GroupEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.parentId = entity.getParent() == null ? -1 : entity.getParent().getId();
    }

    public GroupJson(int id, String name, int parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
