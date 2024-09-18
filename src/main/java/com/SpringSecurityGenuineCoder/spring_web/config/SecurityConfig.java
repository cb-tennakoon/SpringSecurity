package com.SpringSecurityGenuineCoder.spring_web.config;


import com.SpringSecurityGenuineCoder.spring_web.service.AuthenticationSuccessHandler;
import com.SpringSecurityGenuineCoder.spring_web.service.MyUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetail myUserDetail;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->{
                    registry.requestMatchers("/home","/register/**").permitAll();
                    registry.requestMatchers("/admin/**").hasRole("ADMIN");
                    registry.requestMatchers("/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer ->{
                        httpSecurityFormLoginConfigurer
                                .loginPage("/login")
                                .successHandler(new AuthenticationSuccessHandler())
                                .permitAll();
                })
                .build();

    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails1 = User.builder()
//                .username("user")
//                .password("$2a$12$WlvfqHzelhLJDTnvAkNCse1TM5OAMcKkZovLrJDV4DX6IGW0fy/QS")
//                .roles("USER")
//                .build();
//
//        UserDetails userDetails2 = User.builder()
//                .username("admin")
//                .password("$2a$12$xttuB5AlCnGTT6GMPC9FMueC.4KGTxO7iIsR5/eQW332cGQ3Sfae6")
//                .roles("USER","ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails1,userDetails2);
//    }

    @Bean
    public UserDetailsService userDetailsService(){
        return myUserDetail;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetail);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
//    type of password encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
