package Donkey.Api.JSON.Group;

import Donkey.Database.Entity.GroupEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

public class GroupJson {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public int id;
    public String name;
    public int parentId;
    public boolean haveChildrenGroup;

    public GroupJson(){

    }

    public GroupJson(GroupEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.parentId = entity.getParent() == null ? -1 : entity.getParent().getId();
        this.haveChildrenGroup = entity.haveChildrenGroup();

    }

    public GroupJson(int id, String name, int parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
