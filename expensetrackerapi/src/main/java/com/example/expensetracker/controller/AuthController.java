package com.example.expensetracker.controller;


import com.example.expensetracker.entity.AuthModel;
import com.example.expensetracker.entity.JwtResponse;
import com.example.expensetracker.entity.User;
import com.example.expensetracker.entity.UserModel;
import com.example.expensetracker.security.CustomUserDetailsService;
import com.example.expensetracker.service.UserService;
import com.example.expensetracker.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {

        LOGGER.info("Expense Tracker - AuthController - Login - Start");

        authenticate(authModel.getEmail(), authModel.getPassword());

        // We need to generate the Jwt Token.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());

        System.out.println("userDetails : "+userDetails.toString());

        final String token = jwtTokenUtil.generateToken(userDetails);

        LOGGER.info("Expense Tracker - AuthController - Login - End");

        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws Exception {

        LOGGER.info("Expense Tracker - AuthController - authenticate - Start");


        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        }catch (DisabledException e){
            throw new Exception("User Disabled");
        }catch (BadCredentialsException e){
            throw new Exception("Bad Credentials");
        }


        LOGGER.info("Expense Tracker - AuthController - authenticate - End");

    }


    @PostMapping("/register")
    public ResponseEntity<User> save(@Valid @RequestBody UserModel user){
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }


}
