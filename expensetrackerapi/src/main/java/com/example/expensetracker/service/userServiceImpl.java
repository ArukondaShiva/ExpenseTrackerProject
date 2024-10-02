package com.example.expensetracker.service;

import com.example.expensetracker.entity.User;
import com.example.expensetracker.entity.UserModel;
import com.example.expensetracker.exceptions.ItemAlreadyExistsException;
import com.example.expensetracker.exceptions.ResourceNotFoundException;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public User createUser(UserModel user) {

        if(userRepository.existsByEmail(user.getEmail())){
            throw new ItemAlreadyExistsException("User is already registered with email : "+user.getEmail());
        }

        User newUser = new User();
        BeanUtils.copyProperties(user,newUser);
        newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        //newUser.setPassword(newUser.getPassword());
        return userRepository.save(newUser);
    }

    @Override
    public User readUser() {
        Long userId = getLoggedInUser().getId();
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("USer not found for the id : "+userId));
    }

    @Override
    public User updateUser(UserModel user) {
        User existingUser = readUser();
        existingUser.setName(user.getName()!=null ? user.getName() : existingUser.getName());
        existingUser.setEmail(user.getEmail()!=null ? user.getEmail() : existingUser.getEmail());
        existingUser.setPassword(user.getPassword()!=null ? bcryptEncoder.encode(user.getPassword()) : existingUser.getPassword());
        existingUser.setAge(user.getAge()!=null ? user.getAge() : existingUser.getAge());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser() {
        User existingUser = readUser();
        userRepository.delete(existingUser);
    }

    @Override
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found or the emil : "+email));
    }

}
