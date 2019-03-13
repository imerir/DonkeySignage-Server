package Donkey.WebSite.FormClass.User;

import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;

import java.util.List;

public class AddUserForm {
    public String username;
    public String password;
    public List<RolesEntity> roles;
    //public List<UserScreenPrivilege> screenPrivileges;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesEntity> roles) {
        this.roles = roles;
    }

    /*public List<UserScreenPrivilege> getScreenPrivileges() {
        return screenPrivileges;
    }

    public void setScreenPrivileges(List<UserScreenPrivilege> screenPrivileges) {
        this.screenPrivileges = screenPrivileges;
    }*/
}
