package Donkey.Database.Entity.UserAndPrivileges;

import Donkey.Database.Entity.ScreenEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(UserScreenPrivilegeId.class)
public class UserScreenPrivilege{

//    @EmbeddedId //Don't Touch that Bro
//    private UserScreenPrivilegeId id;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
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

//    public UserScreenPrivilegeId getId() {
//        return id;
//    }
//
//    public void setId(UserScreenPrivilegeId id) {
//        this.id = id;
//    }
}
