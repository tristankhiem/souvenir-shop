package com.sgu.agency.services;

public interface IAuthenticationFacade {
    boolean hasPermission(String permissionCode);
}
