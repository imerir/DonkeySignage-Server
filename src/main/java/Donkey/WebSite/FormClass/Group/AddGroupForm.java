package Donkey.WebSite.FormClass.Group;

import Donkey.Database.Entity.GroupEntity;

public class AddGroupForm {
    private String name;
    private GroupEntity parent;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupEntity getParent() {
        return parent;
    }

    public void setParent(GroupEntity parent) {
        this.parent = parent;
    }
}
