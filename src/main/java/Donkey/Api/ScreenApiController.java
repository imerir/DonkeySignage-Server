package Donkey.Api;

import Donkey.Api.JSON.TemporalRegisterJson;
import Donkey.Database.Entity.TemporalRegister;
import Donkey.Database.Repository.TemporalRegisterRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/screen")
public class ScreenApiController {
    private final TemporalRegisterRepository tmpRegisterRep;
    private Logger log = LogManager.getLogger();

    @Autowired
    public ScreenApiController(TemporalRegisterRepository tmpRegisterRep) {
        this.tmpRegisterRep = tmpRegisterRep;
    }

    @RequestMapping(value = {"/getToken"}, method = RequestMethod.GET)
    public TemporalRegisterJson getToken(HttpServletRequest request){
        TemporalRegister newTmpRegister;
        if(tmpRegisterRep.getTemporalRegisterByIp(request.getRemoteAddr()) == null){
            newTmpRegister = new TemporalRegister(request.getRemoteAddr(),generateCheckToken(),generateUuid(),generateExpirationDateLocalDate());
        }else
        {
            newTmpRegister = tmpRegisterRep.getTemporalRegisterByIp(request.getRemoteAddr());
            newTmpRegister.setTempToken(generateCheckToken());
            newTmpRegister.setExpirationDate(generateExpirationDateLocalDate());
        }
        tmpRegisterRep.save(newTmpRegister);
        return new TemporalRegisterJson(newTmpRegister.getTempToken(),newTmpRegister.getUuid(),generateExpirationDateStr());
    }

    @RequestMapping(value = {"/isRegistered"}, method = RequestMethod.GET)
    public ResponseEntity<String> isRegistered (HttpServletRequest request){
        if(tmpRegisterRep.getTemporalRegisterByIp(request.getRemoteAddr()) != null){
            return new ResponseEntity<>("", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
    }



    /**
     * Generate short check token
     * @return check token as string
     */
    private String generateCheckToken(){
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
    private String generateUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Generate Day Date + 2 day
     * @return Date as LocalDate
     */
    private LocalDate generateExpirationDateLocalDate(){
        LocalDate expirationDate = LocalDate.now().plusDays(2);
        return expirationDate;
    }

    /**
     * Generate Day Date + 2 day
     * @return Date as string
     */
    private String generateExpirationDateStr(){
        LocalDate expirationDate = LocalDate.now().plusDays(2);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return expirationDate.format(formatters);
    }
}
