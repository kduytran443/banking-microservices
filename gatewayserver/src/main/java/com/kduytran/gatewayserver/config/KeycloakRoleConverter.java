package com.kduytran.gatewayserver.config;

import com.kduytran.gatewayserver.utils.RoleUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<GrantedAuthority> grantedAuthorities = ((List<String>) realmAccess.get("roles"))
                .stream().map(RoleUtils::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return grantedAuthorities;
    }
}
