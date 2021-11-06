package com.sgu.agency.configuration.security.jwt;

import com.sgu.agency.services.IAuthenticationFacade;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    private UserPrinciple getUserPrinciple() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public boolean hasPermission(String permissionCode) {
        Optional<String> isHavingPermission = this.getUserPrinciple()
                .getAuthorities()
                .stream()
                .map(granted -> granted.getAuthority())
                .filter(authority -> authority.equals(permissionCode))
                .findFirst();

        if (isHavingPermission.isEmpty()) {
            return false;
        }

        return true;
    }
}
