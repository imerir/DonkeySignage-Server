package Donkey.Api.JSON;

public class Error {
    public String code;
    public String message;

    public Error(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public Error() {
    }
}
