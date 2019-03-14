package Donkey.Api.JSON.User;

public class ModifyMyAccountJson {

    public String username;
    public String currentPassword;
    public String newPassword;

    public ModifyMyAccountJson(String username, String currentPassword, String newPassword) {
        this.username = username;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public ModifyMyAccountJson(){

    }
}
