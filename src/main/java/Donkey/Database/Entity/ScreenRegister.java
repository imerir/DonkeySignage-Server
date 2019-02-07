package Donkey.Database.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScreenRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String ip;
    private String token;
    private String uuid;
    private String name;
    //private Groupe groupe;

    public ScreenRegister(){

    }

    public ScreenRegister(String token, String uuid, String name /*,Groupe groupe*/){
        this.token = token;
        this.uuid = uuid;
        this.name = name;
        //this.groupe = groupe;
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
}
