//package by.vyun.bgb.service;
//
//
//import by.vyun.bgb.convertor.UserToDtoConverter;
//import by.vyun.bgb.dto.UserDto;
//import by.vyun.bgb.exception.UserException;
//import by.vyun.bgb.repository.CityRepo;
//import by.vyun.bgb.repository.UserRepo;
//import by.vyun.bgb.entity.User;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//import static by.vyun.bgb.entity.Const.DEFAULT_AVATAR;
//
//@Data
//@Service
//public class SecurityRestService implements UserDetailsService {
//    private UserRepo userRepo;
//
//
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(5);
//    }
//
//    public SecurityRestService(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
//        User user = userRepo.getFirstByLogin(login);
//        if (user == null) {
//            throw new UsernameNotFoundException(
//                    "No user found with username: " + login);
//        }
//        return new org.springframework.security.core.userdetails.User
//                (user.getLogin(),
//                        user.getPassword(),
//                        getAuthorities(user.getRoles()));
//    }
//
//    private static List<GrantedAuthority> getAuthorities(Set<String> roles) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (String role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
//        return authorities;
//    }
//
//
//}
