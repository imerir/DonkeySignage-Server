package Donkey.WebSite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ErrorCode {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return null;
        }
    }
}
