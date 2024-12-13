package com.ampersandor.cotopia.service;    

import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateTeamId(Long userId, Long teamId) {
        userRepository.updateTeamId(userId, teamId);
    }

    @Transactional
    @Override
    public void deleteTeamId(Long userId) {
        userRepository.deleteTeamId(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
