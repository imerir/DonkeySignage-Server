package Donkey.WebSite.FormClass;

import Donkey.Database.Entity.Groupe;

public class ScreenRegisterForm {

    public String name;
    public String uuid;
    //private Groupe groupe;

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
