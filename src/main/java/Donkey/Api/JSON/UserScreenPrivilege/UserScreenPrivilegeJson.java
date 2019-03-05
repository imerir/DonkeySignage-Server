package Donkey.Api.JSON.UserScreenPrivilege;

public class UserScreenPrivilegeJson {

    public Integer userId;
    public Integer screenId;
    public String privilege;

    public UserScreenPrivilegeJson(){

    }

    public UserScreenPrivilegeJson(Integer userId, Integer screenId, String privilege) {
        this.userId = userId;
        this.screenId = screenId;
        this.privilege = privilege;
    }

}
