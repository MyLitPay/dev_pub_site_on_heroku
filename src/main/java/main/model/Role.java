package main.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.READ, Permission.WRITE, Permission.UPDATE)),
    MODERATOR(Set.of(Permission.READ, Permission.WRITE, Permission.UPDATE,
            Permission.DELETE, Permission.MODERATE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getAuthority()))
                .collect(Collectors.toSet());
    }
}
