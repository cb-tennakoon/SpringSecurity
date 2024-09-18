package com.SpringSecurityGenuineCoder.spring_web.service;

import com.SpringSecurityGenuineCoder.spring_web.model.MyUser;
import com.SpringSecurityGenuineCoder.spring_web.repository.MyUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetail  implements UserDetailsService {
    @Autowired
    private MyUserRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.findByUsername(username);

        if(user.isPresent()){
            var userObj = user.get();
            System.out.println(userObj.getPassword());
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        }
        else{
            throw new UsernameNotFoundException(username);
        }

    }
    private String[] getRoles(MyUser myUser){
        if(myUser.getRole()==null){
            return new String[]{"USER"};
        }
        return myUser.getRole().split(",");
    }
}
