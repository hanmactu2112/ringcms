package com.ringme.cms.repository;

import com.ringme.cms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "Select * from user u where u.user_name = ?1",nativeQuery = true)
    Optional<User> findUserByUserName(String username);

    @Query(value = "Select * from user u where u.id =?1",countQuery = "Select count(*) from user u where u.id =?1",nativeQuery = true)
    Page<User> findAllUserById(Long id, Pageable pageable);
    @Query(value = "Select * from user u where u.user_name like %?1%",countQuery = "Select count(*) from user u where u.user_name like %?1%",nativeQuery = true)
    Page<User> findAllUserByUsername(String username,Pageable pageable);
    @Query(value = "Select * from user u where u.user_type =?1",countQuery = "Select count(*) from user u where u.user_type =?1",nativeQuery = true)
    Page<User> findAllUserByType(String type, Pageable pageable);
    @Query(value = "Select * from user u where u.id =?1 and u.user_name like %?2%",countQuery = "Select count(*) from user u where u.id =?1 and u.user_name like %?2%",nativeQuery = true)
    Page<User> findAllUserByIdAndUsername(Long id, String username, Pageable pageable);
    @Query(value = "Select * from user u where u.id =?1 and u.user_type =?2",countQuery = "Select count(*) from user u where u.id =?1 and u.user_type =?2",nativeQuery = true)
    Page<User> findAllUserByIdAndType(Long id, String type, Pageable pageable);
    @Query(value = "Select * from user u where u.user_name like %?1% and u.user_type =?2",countQuery = "Select count(*) from user u where u.user_name like %?1% and u.user_type =?2",nativeQuery = true)
    Page<User> findAllUserBy(String username, String type, Pageable pageable);
    @Query(value = "Select * from user u where u.id =?1 and u.user_name like %?2% and u.user_type =?3",countQuery = "Select count(*) from user u where u.id =?1 and u.user_name like %?2% and u.user_type =?3",nativeQuery = true)
    Page<User> findAllUserBy(Long id, String username, String type, Pageable pageable);
}
