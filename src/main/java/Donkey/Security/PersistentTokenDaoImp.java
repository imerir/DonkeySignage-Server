package Donkey.Security;

import Donkey.Database.Entity.PersistentLoginEntity;
import Donkey.Database.Repository.PersistentTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;


@Repository()
@Transactional
public class PersistentTokenDaoImp implements org.springframework.security.web.authentication.rememberme.PersistentTokenRepository {

    private Logger logger = LogManager.getLogger();
    private final PersistentTokenRepository persistentTokenRepository;

    @Autowired
    public PersistentTokenDaoImp(PersistentTokenRepository persistentTokenRepository) {
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLoginEntity ple = new PersistentLoginEntity();
        ple.setSeries(token.getSeries());
        ple.setToken(token.getTokenValue());
        ple.setUsername(token.getUsername());
        ple.setLastUsed(token.getDate());
        persistentTokenRepository.save(ple);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentLoginEntity ple = persistentTokenRepository.getBySeries(series);
        ple.setToken(tokenValue);
        ple.setLastUsed(lastUsed);
        persistentTokenRepository.save(ple);

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLoginEntity ple = persistentTokenRepository.getBySeries(seriesId);
        if(ple != null)
            return new PersistentRememberMeToken(ple.getUsername(), ple.getSeries(), ple.getToken(), ple.getLastUsed());

        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        persistentTokenRepository.deleteByUsername(username);
    }
}
