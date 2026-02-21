package com.codewithdurgesh.blog.services.impl;

import java.util.List;
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
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    // ================= REGISTER =================
    // 🔥 MOST IMPORTANT METHOD
    @Override
    @Transactional
    public UserDto registerNewUser(UserDto userDto) {

        // DTO → ENTITY
        User user = this.modelMapper.map(userDto, User.class);

        // encode password
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        // STEP 1: save user FIRST (generate ID)
        User savedUser = this.userRepo.save(user);

        // STEP 2: fetch role
        Role role = this.roleRepo
                .findById(AppConstants.NORMAL_USER)
                .orElseThrow(() -> new RuntimeException("NORMAL_USER role missing in DB"));

        // STEP 3: attach role AFTER ID exists
        savedUser.getRoles().add(role);

        // STEP 4: save again (creates entry in user_roles)
        User finalUser = this.userRepo.save(savedUser);

        return this.modelMapper.map(finalUser, UserDto.class);
    }

    // ================= CREATE USER =================
    @Override
    public UserDto createUser(UserDto userDto) {

        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    // ================= UPDATE =================
    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());

        User updated = this.userRepo.save(user);
        return this.modelMapper.map(updated, UserDto.class);
    }

    // ================= GET ONE =================
    @Override
    public UserDto getUserById(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        return this.modelMapper.map(user, UserDto.class);
    }

    // ================= GET ALL =================
    @Override
    public List<UserDto> getAllUsers() {
        return this.userRepo.findAll()
                .stream()
                .map(user -> this.modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    // ================= DELETE =================
    @Override
    public void deleteUser(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        this.userRepo.delete(user);
    }
}