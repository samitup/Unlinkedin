package fi.samit.unlinked.service.Controllers;

import fi.samit.unlinked.service.services.CustomUserDetailsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.context.request.RequestContextHolder;

public class UserController {       //kesken

    @Autowired
    private SessionRegistry sessionRegistry;

    public void listLoggedInUsers() {

        sessionRegistry.registerNewSession( RequestContextHolder.currentRequestAttributes().getSessionId(),SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (final Object principal : allPrincipals) {
            if (principal instanceof CustomUserDetailsService) {
                final CustomUserDetailsService user = (CustomUserDetailsService) principal;

                List<SessionInformation> activeUserSessions
                        = sessionRegistry.getAllSessions(principal,
                                /* includeExpiredSessions */ false); 

                if (!activeUserSessions.isEmpty()) {
                    System.out.println(user);
                }
            }
        }
    }
}
