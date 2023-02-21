package com.ringme.cms.repository;

import com.ringme.cms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "Select * from user u where u.user_name = ?1",nativeQuery = true)
    Optional<User> findUserByUserName(String username);
}
