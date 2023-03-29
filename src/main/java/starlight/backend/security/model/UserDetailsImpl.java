package starlight.backend.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import starlight.backend.security.model.enums.Role;

import java.util.Collection;
import java.util.List;


public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;
    private Role role;

    public UserDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = role.TALENT;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
