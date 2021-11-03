package com.sgu.agency.services;

public interface IAuthenticationFacade {

    String getAgencyId();

    String getCompanyId();

    boolean hasPermission(String permissionCode);
}
