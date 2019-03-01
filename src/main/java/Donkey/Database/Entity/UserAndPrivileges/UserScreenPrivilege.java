package Donkey.Database.Entity.UserAndPrivileges;

import Donkey.Database.Entity.ScreenEntity;

import javax.persistence.*;

@Entity
public class UserScreenPrivilege {

    @EmbeddedId
    private UserScreenPrivilegeId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("screenId")
    private ScreenEntity screenEntity;

    private String privilege;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public ScreenEntity getScreenEntity() {
        return screenEntity;
    }

    public void setScreenEntity(ScreenEntity screenEntity) {
        this.screenEntity = screenEntity;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public UserScreenPrivilegeId getId() {
        return id;
    }

    public void setId(UserScreenPrivilegeId id) {
        this.id = id;
    }
}
