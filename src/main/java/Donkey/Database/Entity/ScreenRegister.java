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

    public ScreenRegister(){

    }

    public ScreenRegister(String ip, String token, String uuid){
        this.ip = ip;
        this.token = token;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
