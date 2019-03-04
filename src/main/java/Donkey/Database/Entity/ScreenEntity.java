package Donkey.Database.Entity;

import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ScreenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String ip;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    private String uuid;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private TemplateEntity template;

    @OneToMany(
            mappedBy = "screenEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<UserScreenPrivilege> userPrivilege = new ArrayList<>();


    public ScreenEntity(){

    }

    public ScreenEntity(String token, String uuid, String name ,GroupEntity group){
        this.token = token;
        this.uuid = uuid;
        this.name = name;
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String tempToken) {
        this.token = tempToken;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public TemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(TemplateEntity template) {
        this.template = template;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public List<UserScreenPrivilege> getUserPrivilege() {
        return userPrivilege;
    }

    public void setUserPrivilege(List<UserScreenPrivilege> userPrivilege) {
        this.userPrivilege = userPrivilege;
    }
}
