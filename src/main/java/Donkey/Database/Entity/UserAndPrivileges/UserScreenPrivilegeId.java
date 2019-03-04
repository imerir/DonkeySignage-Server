package Donkey.Database.Entity.UserAndPrivileges;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserScreenPrivilegeId implements Serializable {
    private int userEntity;
    private int screenEntity;

    public UserScreenPrivilegeId() {
    }


    public UserScreenPrivilegeId(int userEntity, int screenEntity) {
        this.userEntity = userEntity;
        this.screenEntity = screenEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEntity, screenEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if( obj == null || getClass() != obj.getClass())
            return false;

        UserScreenPrivilegeId that = (UserScreenPrivilegeId) obj;
        return Objects.equals(userEntity, that.userEntity) && Objects.equals(screenEntity, that.screenEntity);
    }

    public int getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(int userEntity) {
        this.userEntity = userEntity;
    }

    public int getScreenEntity() {
        return screenEntity;
    }

    public void setScreenEntity(int screenEntity) {
        this.screenEntity = screenEntity;
    }
}
