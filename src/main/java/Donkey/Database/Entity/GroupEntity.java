package Donkey.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<ScreenEntity> screen = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private GroupEntity parent;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List <GroupEntity> childrens = new ArrayList<>();

    public GroupEntity(){

    }

    public GroupEntity(String name, List<ScreenEntity> screenList, GroupEntity parent, List<GroupEntity> childrens){
        this.setName(name);
        for(ScreenEntity screen : screenList){
            this.getScreenList().add(screen);
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

    public String toString(){
        String str;

        if(getParent() == null)
            str = "GroupEntity id : " + this.getId() + ", Name : " + getName() + ", Parent id : " + null;
        else
            str = "GroupEntity id : " + this.getId() + ", Name : " + getName() + ", Parent id : " + getParent().getId();

        for(int i = 0 ; i < getChildrens().size() ; i++){
            str = ", Children id : " + getChildrens().get(i);
        }

        for(int i = 0 ; i < getScreenList().size() ; i++){
            str = ", Screen id : " + getScreenList().get(i);
        }

        return str;
    }

    public boolean haveChildrenGroup(){
        return getChildrens().size() != 0;
    }
}
