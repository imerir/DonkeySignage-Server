package Donkey.Tools;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.SpringContext;
import Donkey.Tools.Exception.TokenNotMatch;
import Donkey.Tools.Exception.UnknownUUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ScreenTools {
    private Logger log = LogManager.getLogger();

    private ScreenRepository screenRepository;

    private static ScreenTools INSTANCE;

    private ScreenTools(){
        ApplicationContext context = SpringContext.getAppContext();
        screenRepository = (ScreenRepository) context.getBean("screenRepository");
    }

    public static ScreenTools getInstance(){
        return (INSTANCE == null) ? new ScreenTools() : INSTANCE;
    }

    /**
     * Generate Token
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
        return LocalDate.now().plusDays(2);
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

    public ScreenEntity screenLogin(String uuid, String token) throws UnknownUUID, TokenNotMatch {
        ScreenEntity screenEntity = screenRepository.getScreenRegisterByUuid(uuid);
        if(screenEntity == null)
            throw new UnknownUUID();

        if(!screenEntity.getToken().equals(token))
            throw new TokenNotMatch();

        return screenEntity;
    }

}
