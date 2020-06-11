//package by.vyun.bgb.config;
//
//import by.vyun.bgb.service.SecurityRestService;
//import by.vyun.bgb.service.SecurityUserService;
//import lombok.Data;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//    @Data
//    @EnableWebSecurity
//    public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
//        //@Autowired
//        //private PasswordEncoder passwordEncoder;
//        private final SecurityRestService securityRestService;
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(securityRestService);
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests().anyRequest().authenticated()
//                    .and().httpBasic()
//                    .and().sessionManagement().disable();
//        }
//
//
//        //@Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder(5);
//        }
//    }
//
