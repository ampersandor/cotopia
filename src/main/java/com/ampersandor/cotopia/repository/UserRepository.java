package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.User;
// import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void delete(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateTeamId(Long userId, Long teamId);
    void deleteTeamId(Long userId);
}

// public interface UserRepository extends JpaRepository<User, Long> {
//     Optional<User> findByUsername(String username);
//     boolean existsByUsername(String username);
//     boolean existsByEmail(String email);
// }
