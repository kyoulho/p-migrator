package io.playce.migrator.dto.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Setter
@Getter
public class SecurityUser implements UserDetails, Serializable {

    private String userLoginId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String loginFailureCnt;
    private Boolean lockYn;

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
