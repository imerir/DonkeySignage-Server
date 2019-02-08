package Donkey.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ScreenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String ip;
    private String token;
    private String uuid;
    private String name;

    @ManyToOne
    @JoinColumn(nullable = true)
    @JsonIgnore
    private GroupEntity group;

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

    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}
