package by.vyun.bgb.service;

import by.vyun.bgb.convertor.UserToDtoConverter;
import by.vyun.bgb.dto.UserDto;
import by.vyun.bgb.exception.UserException;
import by.vyun.bgb.repository.CityRepo;
import by.vyun.bgb.repository.UserRepo;
import by.vyun.bgb.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static by.vyun.bgb.entity.Const.DEFAULT_AVATAR;

@Data
@Service
public class SecurityUserService implements UserDetailsService {
    private UserRepo userRepo;
    private CityRepo cityRepo;
    private CityService cityService;
    private UserToDtoConverter userConverter;
    private Validator validator;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    public SecurityUserService(UserRepo userRepo, CityRepo cityRepo,
                               CityService cityService,
                               UserToDtoConverter userConverter) {
        this.userRepo = userRepo;
        this.cityRepo = cityRepo;
        this.cityService = cityService;
        this.userConverter = userConverter;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepo.getFirstByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "No user found with username: " + login);
        }
        boolean enabled = user.getIsEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = user.getIsActive();
        return new org.springframework.security.core.userdetails.User
                (user.getLogin(),
                        user.getPassword(),
                        enabled,
                        accountNonExpired,
                        credentialsNonExpired,
                        accountNonLocked,
                        getAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> getAuthorities(Set<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public void registration(User user, String passwordConfirm, String cityName) throws UserException, IOException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new UserException(violations.iterator().next().getMessage());
        }
        if (user.checkPassword(passwordConfirm)) {
            throw new UserException("Different password and confirmation");
        }
        if (userRepo.getFirstByLogin(user.getLogin()) != null) {
            throw new UserException("Login duplicated");
        }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        if (user.getAvatar().isEmpty()) {
            user.setAvatar(DEFAULT_AVATAR);
        }
        user.setCity(cityRepo.getFirstByName(cityName));
        userRepo.save(user);
    }

    public UserDto signIn(String login, String password) throws UserException {
        User foundedUser = userRepo.getFirstByLogin(login);
        if (foundedUser == null) {
            throw new UserException("Login not found");
        }
        if (!passwordEncoder().matches(password, foundedUser.getPassword())) {
            throw new UserException("Invalid password");
        }
        UserDto userDto = userConverter.convert(foundedUser);
        return userDto;
    }

    public User update(User currentUser, User changedUser,
                       String newPassword, String newPasswordConfirm,
                       String cityName) throws UserException {
        if (changedUser.getPassword().isEmpty()) {
            throw new UserException("Enter current password");
        }
        if (!passwordEncoder().matches(changedUser.getPassword(), currentUser.getPassword())) {
            throw new UserException("Invalid current password");
        }
        if (!newPassword.isEmpty() || !newPasswordConfirm.isEmpty()) {
            if (!newPassword.equals(newPasswordConfirm)) {
                throw new UserException("Different new password and confirmation");
            } else {
                currentUser.setPassword(passwordEncoder().encode(newPassword));
            }
        }
        if (cityName != null && !cityName.isEmpty()) {
            currentUser.setCity(cityService.getCityByName(cityName));
        }
        if (!changedUser.getAvatar().isEmpty()) {
            currentUser.setAvatar(changedUser.getAvatar());
        }
        currentUser.setAddress(changedUser.getAddress());
        return userRepo.saveAndFlush(currentUser);
    }


}
