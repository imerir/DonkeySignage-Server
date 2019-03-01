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

}
