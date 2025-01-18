package com.uexcel.executive.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public List<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null ||realmAccess.isEmpty()) {
            return List.of();
        }
        List<GrantedAuthority> Roles =((List<String>) realmAccess.get("roles"))
                .stream().map(roleName->"ROLE_"+roleName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
        return Roles;
    }

}
