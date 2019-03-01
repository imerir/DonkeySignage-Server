package Donkey.Security;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    private Logger logger = LogManager.getLogger();
    /**
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
                            hasScreenPermision((ScreenEntity) screen, (String) permission, (UserEntity) authentication.getPrincipal());
                        }
                    }
                }
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
        logger.debug("ups");
        return false;
    }

    private boolean hasScreenPermision(ScreenEntity screenEntity, String permission, UserEntity userEntity){
        return false;
    }
}
