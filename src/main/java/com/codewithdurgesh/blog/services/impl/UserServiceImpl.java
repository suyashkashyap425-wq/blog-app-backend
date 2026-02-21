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

    // ================= REGISTER USER (MOST IMPORTANT) =================
    @Override
    @Transactional
    public UserDto registerNewUser(UserDto userDto) {

        // 🔥 1. CHECK DUPLICATE EMAIL
        Optional<User> existing = userRepo.findByEmail(userDto.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("User already exists with this email !!");
        }

        // 🔥 2. DTO → ENTITY
        User user = modelMapper.map(userDto, User.class);

        // 🔥 3. ENCODE PASSWORD
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 🔥 4. SAVE USER FIRST (generate ID)
        User savedUser = userRepo.save(user);

        // 🔥 5. ATTACH ROLE AFTER USER ID EXISTS
        Role role = roleRepo.findById(AppConstants.NORMAL_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_NORMAL not found in DB"));

        savedUser.getRoles().add(role);

        // 🔥 6. SAVE AGAIN (insert into user_roles table)
        User finalUser = userRepo.save(savedUser);

        // 🔥 7. RETURN DTO
        return modelMapper.map(finalUser, UserDto.class);
    }

    // ================= CREATE USER =================
    @Override
    public UserDto createUser(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);
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