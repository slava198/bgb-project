package by.vyun.bgb.service;

import by.vyun.bgb.entity.Image;
import by.vyun.bgb.exception.RegistrationException;
import by.vyun.bgb.repository.CityRepo;
import by.vyun.bgb.repository.ImageRepo;
import by.vyun.bgb.repository.UserRepo;
import by.vyun.bgb.entity.User;
import lombok.AllArgsConstructor;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static by.vyun.bgb.entity.Const.DEFAULT_AVATAR;

@Data
@Service
//@AllArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private UserRepo userRepo;
    private ImageRepo imageRepo;
    private CityRepo cityRepo;
    private CityService cityService;
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    public SecurityUserService(UserRepo userRepo, ImageRepo imageRepo, CityRepo cityRepo, CityService cityService, EmailService emailService) {
        this.userRepo = userRepo;
        this.imageRepo = imageRepo;
        this.cityRepo = cityRepo;
        this.emailService = emailService;
        this.cityService = cityService;
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


    public User registration(User user, String cityName, MultipartFile imageFile) throws RegistrationException, IOException {
        if (user.getLogin().trim().length() * user.getPassword().trim().length() * cityName.trim().length() == 0) {
            throw new RegistrationException("Empty login, password or location field");
        }
        if (userRepo.getFirstByLogin(user.getLogin()) != null) {
            throw new RegistrationException("Login duplicated");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Image avatar = new Image();
        if (imageFile.isEmpty()) {
            avatar.setData(imageRepo.findFirstByName(DEFAULT_AVATAR).getData());
            avatar.setName(user.getLogin());
        } else {
            avatar.setData(imageFile.getBytes());
            avatar.setName(imageFile.getOriginalFilename());
        }
        imageRepo.saveAndFlush(avatar);
        user.setAvatar(avatar);
        user.setCity(cityRepo.getFirstByName(cityName));
        user.setActivationCode(String.valueOf(new Random(100).nextDouble()));
        //new thread
        sendActivationCode(user);
        user = userRepo.save(user);
        return user;
    }

    public void sendActivationCode(User user) {
        emailService.sendSimpleMessage(user.getEmail(), "Activating account", "Your confirmation code is:  " + user.getActivationCode());
    }


    public User signIn(String login, String password) throws RegistrationException {
        User foundedUser = userRepo.getFirstByLogin(login);
        if (foundedUser == null) {
            throw new RegistrationException("Login not found");
        }
        if (foundedUser.checkPassword(passwordEncoder.encode(password))) {
            throw new RegistrationException("Invalid password");
        }
        return foundedUser;
    }


    public User update(User currentUser, User changedUser,
                       String newPassword, String newPasswordConfirm,
                       String cityName, MultipartFile imageFile) throws RegistrationException, IOException {

        if (changedUser.getPassword().isEmpty()) {
            throw new RegistrationException("Enter current password");
        }
        String enc = passwordEncoder.encode(newPassword);
        if (!passwordEncoder.matches(changedUser.getPassword(), currentUser.getPassword())) {
            throw new RegistrationException("Invalid current password");
        }
        if (!newPassword.isEmpty() || !newPasswordConfirm.isEmpty()) {
            if (!newPassword.equals(newPasswordConfirm)) {
                throw new RegistrationException("New password and it's confirmations are the different");
            } else {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            }
        }

        if (cityName != null && !cityName.isEmpty()) {
            currentUser.setCity(cityService.getCityByName(cityName));
        }

        if (!imageFile.isEmpty()) {
            Image avatar = imageRepo.findFirstById(currentUser.getAvatar().getId());
            avatar.setData(imageFile.getBytes());
            avatar.setName(imageFile.getOriginalFilename());
            imageRepo.saveAndFlush(avatar);
            currentUser.setAvatar(avatar);
        }
        currentUser.setAddress(changedUser.getAddress());
        currentUser.setDateOfBirth(changedUser.getDateOfBirth());
        return userRepo.saveAndFlush(currentUser);
    }
}
