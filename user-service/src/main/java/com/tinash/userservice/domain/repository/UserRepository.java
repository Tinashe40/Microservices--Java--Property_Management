package com.tinash.userservice.domain.repository;


import com.tinash.cloud.utility.jpa.BaseDao;
import com.tinash.userservice.domain.model.user.User;
import com.tinash.cloud.utility.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUserProfile_UsernameAndDeletedFalse(String username);
    Optional<User> findByUserProfile_FirstNameAndUserProfile_LastNameAndDeletedFalse(String firstName, String lastName);
    Optional<User> findByEmail_ValueAndDeletedFalse(String email);
    Boolean existsByUserProfile_FirstNameAndUserProfile_LastNameAndDeletedFalse(String firstName, String lastName);
    Boolean existsByEmail_ValueAndDeletedFalse(String email);
    Page<User> findAllByDeletedFalse(Pageable pageable);
    Optional<User> findByIdAndDeletedFalse(Long id);
    Collection<Object> findByEmail_Value(String email);
}