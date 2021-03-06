package it.nextre.academy.springdemo.service.impl;

import it.nextre.academy.springdemo.dto.UserRegistrationDto;
import it.nextre.academy.springdemo.entity.Role;
import it.nextre.academy.springdemo.entity.User;
import it.nextre.academy.springdemo.repository.UserRepository;
import it.nextre.academy.springdemo.service.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Logger log;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User save(UserRegistrationDto registration) {
        log.debug("UserServiceImpl.save");
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }



    public User createAdmin(){
        User user = new User();
        user.setFirstName("Valerio");
        user.setLastName("Radice");
        user.setEmail("admin@nextre.it");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


}//end class
