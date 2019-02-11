package Donkey.Tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class UserTools {
    private Logger log = LogManager.getLogger();

    private static UserTools INSTANCE;

    private UserTools(){

    }

    public static UserTools getInstance(){
        return (INSTANCE == null) ? new UserTools() : INSTANCE;
    }

    /**
     * Generate Token
     * @param isShortToken
     *  isShortToken is a boolean, if true = short token, false = long token
     * @return check token as string
     */
    public String generateCheckToken(){
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        String randomStr = Long.toString( longToken, 16 );
        randomStr = randomStr.substring(0,4);
        randomStr = randomStr.toUpperCase();
        return randomStr;
    }

    /**
     * Generate an Uuid
     * @return Uuid as string
     */
    public String generateUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Generate Day Date + 2 day
     * @return Date as LocalDate
     */
    public LocalDate generateExpirationDateLocalDate(){
        LocalDate expirationDate = LocalDate.now().plusDays(2);
        return expirationDate;
    }

    /**
     * Generate Day Date + 2 day
     * @return Date as string
     */
    public String generateExpirationDateStr(){
        LocalDate expirationDate = LocalDate.now().plusDays(2);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return expirationDate.format(formatters);
    }
}
