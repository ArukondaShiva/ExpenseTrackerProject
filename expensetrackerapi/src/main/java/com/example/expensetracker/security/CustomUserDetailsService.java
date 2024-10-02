package com.example.expensetracker.security;

import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("loadUserByUsername-inside:start");

        User existingUSer = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found for the email : "+email));

        System.out.println(existingUSer.toString());

        logger.info("loadUserByUsername-inside:end");

        return new org.springframework.security.core.userdetails.User(
                existingUSer.getEmail(), existingUSer.getPassword(), new ArrayList<>());
    }


}