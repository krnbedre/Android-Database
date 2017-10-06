package com.dhdigital.lms.modal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 27/09/17.
 */

public class Users implements Serializable {

    private String username;
    private Employee employee;
    private Tenant tenant;
    private TenantConfiguration tenantConfiguration;
    private List<Authority> authorities;
    private List<UserRole> userRoles; /*This attribute is not required*/



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public TenantConfiguration getTenantConfiguration() {
        return tenantConfiguration;
    }

    public void setTenantConfiguration(TenantConfiguration tenantConfiguration) {
        this.tenantConfiguration = tenantConfiguration;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }


}
