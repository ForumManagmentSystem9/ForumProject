package com.example.demo.helpers;

import com.example.demo.models.Creatable;
import com.example.demo.models.Role;
import com.example.demo.models.userfolder.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RestrictHelper {
    public static final String ADMIN_ERROR_MESSAGE = "You are not admin";
    public static final String ADMIN_MODERATOR_ERROR_MESSAGE = "You must be admin or moderator";
    public void isUserAdmin(User user){
        if (!user.getRole().getName().equals(Role.RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    ADMIN_ERROR_MESSAGE);
        }
    }
    public void isUserAdminOrModerator(User user){
        if (!user.getRole().getName().equals(Role.RoleType.ADMIN) && !user.getRole().getName().equals(Role.RoleType.MODERATOR)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ADMIN_MODERATOR_ERROR_MESSAGE);
        }
    }

    public <T extends Creatable> void isUserACreator(T entity, User user) {
        if (user.getId() != entity.getCreator().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        }
    }
    public <T extends Creatable> void deletePermission(T entity, User user) {
        if (user.getId() != entity.getCreator().getId() && (!user.getRole().getName().equals(Role.RoleType.ADMIN) && !user.getRole().getName().equals(Role.RoleType.MODERATOR))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        }
    }
}
