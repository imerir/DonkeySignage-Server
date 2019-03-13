package Donkey.Api.JSON.User;

public class UserJson {
    public String username;
    public Integer roleId;
    public String password;

    public UserJson(){

    }

    public UserJson(String username){
        this.username = username;
    }
}
