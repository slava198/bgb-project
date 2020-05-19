package by.vyun.bgb.config;

import by.vyun.bgb.service.SecurityUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final SecurityUserService securityUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/", "/user/registration").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/user/login")
                //.defaultSuccessUrl("/account", true)
                .successForwardUrl("/account")
                .usernameParameter("login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }


}
