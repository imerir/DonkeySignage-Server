package Donkey.Api;

import Donkey.Api.JSON.ScreenRegisterJson;
import Donkey.Api.JSON.TemporalRegisterJson;
import Donkey.Database.Entity.ScreenRegister;
import Donkey.Database.Entity.TemporalRegister;
import Donkey.Database.Repository.ScreenRegisterRepository;
import Donkey.Database.Repository.TemporalRegisterRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/screen")
public class ScreenApiController {
    private final ScreenRegisterRepository screenRegisterRep;
    private final TemporalRegisterRepository tmpRegisterRep;
    private Logger log = LogManager.getLogger();

    @Autowired
    public ScreenApiController(TemporalRegisterRepository tmpRegisterRep, ScreenRegisterRepository screenRegisterRep) {
        this.tmpRegisterRep = tmpRegisterRep;
        this.screenRegisterRep = screenRegisterRep;
    }

    private final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    public String getClientIpAddress(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            log.debug("Ip bypass : " + ip);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @RequestMapping(value = {"/test"},method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        return  getClientIpAddress(request);
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
    public ResponseEntity<ScreenRegisterJson> isRegistered (@CookieValue(value = "uuid")String uuid){
        log.debug("Uuid send by cookie : " + uuid);
        if(uuid != null && !uuid.equals("")){
            ScreenRegister newScreenRegister = screenRegisterRep.getScreenRegisterByUuid(uuid);
            log.debug("ScreenRegister get in db: " + newScreenRegister);
            if(newScreenRegister != null){
                ScreenRegisterJson newScreenRegisterJson = new ScreenRegisterJson(newScreenRegister.getToken(), newScreenRegister.getUuid());
                log.debug("Send Json " + newScreenRegisterJson);
                return new ResponseEntity<>(newScreenRegisterJson, HttpStatus.OK);
            }else{
                log.debug("ScreenRegister not in db");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }else{
            log.debug("Bad Cookie");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
