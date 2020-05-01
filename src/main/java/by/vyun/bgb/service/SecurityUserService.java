package by.vyun.bgb.service;

import by.vyun.bgb.repository.UserRepo;
import by.vyun.bgb.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private UserRepo userRepo;


    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepo.getFirstByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user found with username: " + login);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = user.getIsActive();
        return new org.springframework.security.core.userdetails.User
                (user.getLogin(),
                        user.getPassword().toLowerCase(), enabled, accountNonExpired,
                        credentialsNonExpired, accountNonLocked,
                        getAuthorities(user.getRoles()));
    }


    private static List<GrantedAuthority> getAuthorities(Set<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
