package com.codewithdurgesh.blog.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codewithdurgesh.blog.config.AppConstants;
import com.codewithdurgesh.blog.entities.Role;
import com.codewithdurgesh.blog.entities.User;
import com.codewithdurgesh.blog.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.blog.payloads.UserDto;
import com.codewithdurgesh.blog.repositories.RoleRepo;
import com.codewithdurgesh.blog.repositories.UserRepo;
import com.codewithdurgesh.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER USER =================
    @Override
    @Transactional
    public UserDto registerNewUser(UserDto userDto) {

        // check duplicate
        Optional<User> existing = userRepo.findByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("User already exists with this email !!");
        }

        // manual mapping (NO MODEL MAPPER HERE)
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());

        // encode password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // save user
        User savedUser = userRepo.save(user);

        // add role
        Role role = roleRepo.findById(AppConstants.NORMAL_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_NORMAL not found"));

        savedUser.getRoles().add(role);
        userRepo.save(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    // ================= CREATE USER =================
    @Override
    public UserDto createUser(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    // ================= UPDATE =================
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());

        User updatedUser = userRepo.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    // ================= GET ONE =================
    @Override
    public UserDto getUserById(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        return modelMapper.map(user, UserDto.class);
    }

    // ================= GET ALL =================
    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @Override
    public void deleteUser(Integer userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        userRepo.delete(user);
    }
}