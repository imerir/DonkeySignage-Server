package Donkey.Security;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.UserScreenPrivilegeRepository;
import Donkey.SpringContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    private Logger logger = LogManager.getLogger();

    private UserScreenPrivilegeRepository userScreenPrivilegeRepository;
    private ScreenRepository screenRepository;


    /**
     * Check if the user have the permission to do this.
     * @param authentication     represents the user in question. Should not be null.
     * @param targetDomainObject the domain object for which permissions should be
     *                           checked. May be null in which case implementations should return false, as the null
     *                           condition can be checked explicitly in the expression.
     * @param permission         a representation of the permission object as supplied by the
     *                           expression system. Not null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        UserEntity loggedUser =  (UserEntity) authentication.getPrincipal();
        if(loggedUser.isAdmin())
            return true;



        if(targetDomainObject instanceof ResponseEntity<?>)
        {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) targetDomainObject;
            if( responseEntity.getBody() instanceof List){
                List<?> body = (List<?>) responseEntity.getBody();
                if(body.size() == 0)
                    return true;
                else{
                    if(body.get(0) instanceof ScreenEntity){
                        for(Object screen : body){
                            if(!hasScreenPermission((ScreenEntity) screen, (String) permission, loggedUser))
                                return false;
                        }
                        return true;
                    }
                }
            }
            else if (responseEntity.getBody() instanceof ScreenEntity){
                return hasScreenPermission((ScreenEntity) responseEntity.getBody(), (String) permission, loggedUser);
            }
        }
        return false;
    }

    /**
     * Alternative method for evaluating a permission where only the identifier of the
     * target object is available, rather than the target instance itself.
     *
     * @param authentication represents the user in question. Should not be null.
     * @param targetId       the identifier for the object instance (usually a Long)
     * @param targetType     a String representing the target's type (usually a Java
     *                       classname). Not null.
     * @param permission     a representation of the permission object as supplied by the
     *                       expression system. Not null.
     * @return true if the permission is granted, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        UserEntity loggedUser =  (UserEntity) authentication.getPrincipal();
        if(loggedUser.isAdmin())
            return true;

        if(screenRepository == null){
            ApplicationContext context = SpringContext.getAppContext();
            screenRepository = (ScreenRepository) context.getBean("screenRepository");
        }
        logger.debug(targetId);
        logger.debug(targetType);
        if(targetType.equals("screen")){
            int id = (int) targetId;
            ScreenEntity screen = screenRepository.getScreenEntityById(id);
            return hasScreenPermission(screen, (String) permission, loggedUser);
        }
        return false;
    }

    private boolean hasScreenPermission(ScreenEntity screenEntity, String permission, UserEntity userEntity){
        if(userScreenPrivilegeRepository == null){
            ApplicationContext context = SpringContext.getAppContext();
            userScreenPrivilegeRepository = (UserScreenPrivilegeRepository) context.getBean("userScreenPrivilegeRepository");
        }
        List<UserScreenPrivilege> privileges = userScreenPrivilegeRepository.getByUserEntityAndScreenEntity(userEntity, screenEntity);
        return privileges.size() != 0;
    }
}
