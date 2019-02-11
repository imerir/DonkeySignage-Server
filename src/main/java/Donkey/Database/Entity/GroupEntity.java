package Donkey.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @OneToMany
    private List<ScreenEntity> screen = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private GroupEntity parent;

    @OneToMany
    @JsonIgnore
    private List <GroupEntity> childrens= new ArrayList<>();

    public GroupEntity(){

    }

    public GroupEntity(String name, List<ScreenEntity> screenList, GroupEntity parent, List<GroupEntity> childrens){
        this.setName(name);
        for(ScreenEntity scree : screenList){
            this.getScreenList().add(scree);
        }
        this.setParent(parent);
        for (GroupEntity children : childrens) {
            this.getChildrens().add(children);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScreenEntity> getScreenList() {
        return screen;
    }

    public void setScreenList(List<ScreenEntity> screenList) {
        this.screen = screenList;
    }

    public GroupEntity getParent() {
        return parent;
    }

    public void setParent(GroupEntity parent) {
        this.parent = parent;
    }

    public List<GroupEntity> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<GroupEntity> childrens) {
        this.childrens = childrens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
