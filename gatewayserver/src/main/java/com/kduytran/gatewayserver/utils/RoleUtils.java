package com.kduytran.gatewayserver.utils;

public class RoleUtils {

    private RoleUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String getRoleName(String role) {
        return String.format("ROLE_%s", role);
    }

}
