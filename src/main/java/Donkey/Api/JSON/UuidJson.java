package Donkey.Api.JSON;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UuidJson {
    public String uuid;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String message;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid, String message) {
        this.uuid = uuid;
        this.message = message;
    }
}
