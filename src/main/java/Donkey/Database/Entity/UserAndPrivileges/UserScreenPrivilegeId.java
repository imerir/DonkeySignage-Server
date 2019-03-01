package Donkey.Database.Entity.UserAndPrivileges;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserScreenPrivilegeId implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "screen_id")
    private int screenId;

    public UserScreenPrivilegeId() {
    }

    public UserScreenPrivilegeId(int userId, int screenId) {
        this.userId = userId;
        this.screenId = screenId;
    }


    @Override
    public int hashCode() {
        return Objects.hash(userId, screenId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if( obj == null || getClass() != obj.getClass())
            return false;

        UserScreenPrivilegeId that = (UserScreenPrivilegeId) obj;
        return Objects.equals(userId, that.userId) && Objects.equals(screenId, that.screenId);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }
}
