package com.toni.virtualpet.model.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("Base user") ,
    ROLE_ADMIN("Admin user") ,
    ROLE_SUPER_ADMIN("Super Admin");

    private final String label;
}
