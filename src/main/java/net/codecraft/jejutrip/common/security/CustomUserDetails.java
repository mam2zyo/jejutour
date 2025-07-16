package net.codecraft.jejutrip.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter  // UserDetails 구현 클래스가 꼭 필요한 이유가 뭐야? 별로 하는 일도 없는 것 같은데....
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getKey())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // OIDC 이용자는 null 이 반환되나?
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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